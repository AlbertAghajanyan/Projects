import { NextFunction, Request, Response } from "express";
import { APP_RESPONSES } from "../appResponse";
import { db } from "./database";
import * as database from "./database";
import { DataBase } from "./database";

import { ResponseObject } from "./responseObject";

import * as fs from "fs";
import { serverLogger } from "../utils/logger";

export class Objects {
    private objectList: ResponseObject[];

    // response codes
    private notFound: any;
    private internalErr: any;
    private internalDBErr: any;
    private incorrectParam: any;
    private success: any;
    private created: any;

    constructor() {
        this.objectList =  new Array<ResponseObject>();

        this.notFound = APP_RESPONSES.development.not_found;
        this.internalErr = APP_RESPONSES.development.internal_error;
        this.internalDBErr = APP_RESPONSES.development.internal_dbs_error;
        this.incorrectParam = APP_RESPONSES.development.incorrect_param;
        this.success = APP_RESPONSES.development.ok;
        this.created = APP_RESPONSES.development.created;
    }

    public getObjectList(req: Request, res: Response) {
        const start: number = +(req.query.start >= 0 ? req.query.start : 0);
        const size: number = +(req.query.size >= 0 ? req.query.size : 10);
        db.any("select traffic.id from traffic order by traffic.id")
            .then((data) => {
                const countObjects = data.length;
                const next = data[start].id;
                const last = (start + size) < data.length ? data[start + size].id : data[data.length - 1].id;
                db.any("select traffic.*, image.*, cameras.camera_name from image,cameras, traffic where \
                        image.traffic_id  = traffic.id and traffic.id <= $2 and  traffic.id >= $1\
                        and image.cameras_id = cameras.id  order by traffic.id", [next, last])
                    .then((newData) => {
                        this.getObjectWithDBData(newData);
                        res.status(this.success.httpCode).send({objects: this.objectList, count: countObjects});
                    });
            })
            .catch ((err) => {
                res.status(this.internalDBErr.httpCode).json(this.internalDBErr.body);
            });
    }

    public validateSearch(req: Request) {
        const obj = {
            camera_name: req.query.camera_name,
            from_date: req.query.from_date,
            to_date: req.query.to_date,
            type: req.query.type,
        };

        if (req.query.camera_name === "undefined" || req.query.camera_name === "") {
            obj.camera_name = "%";
        }
        if (req.query.type === "undefined") {
            obj.type = "";
        } else if (req.query.type) {
            obj.type = "traffic.type = '" + req.query.type + "' and ";
        }
        if (isNaN(req.query.from_date)) {
            obj.from_date = 1483257780;
        }
        if (isNaN(req.query.to_date)) {
            obj.to_date = Date.now();
        }
        return obj;
    }

    public searchObject(req: Request, res: Response) {
        const obj = this.validateSearch(req);
        console.log(obj);
        db.any("select traffic.*, image.*, cameras.camera_name from image,cameras, traffic where\
                image.traffic_id = traffic.id and cameras.id = image.cameras_id and \
                cameras.camera_name like $1 and " + obj.type + "traffic.first_detected_date\
                between TIMESTAMP WITH TIME ZONE 'epoch' + " + obj.from_date + "* INTERVAL '1 second'\
                and TIMESTAMP WITH TIME ZONE 'epoch' + " + obj.to_date + "* INTERVAL '1 second'", [obj.camera_name])
            .then((data) => {
                this.getObjectWithDBData(data);
                serverLogger.logger.info(data);
                res.status(this.success.httpCode).json({objects: this.objectList});
            })
            .catch ((err) => {
                serverLogger.logger.info(err);
                res.status(this.internalDBErr.httpCode).json(this.internalDBErr.body);
            });
    }

    private imagesNames(folderName) {
        let imagesURL = new Array<string>();
        if (fs.existsSync(folderName)) {
            imagesURL = fs.readdirSync(folderName);
            for (let i = 0; i < imagesURL.length; ++i) {
                imagesURL[i] = folderName + "/" + imagesURL[i];
            }
        }
        return imagesURL;
    }

    private imagesEncode(images: string[]) {
        const imagesEncode = new Array<string>();
        for (const image of images) {
            const originalData = fs.readFileSync(String(image));
            const base64Image = originalData.toString("base64");
            imagesEncode[imagesEncode.length] = base64Image;
        }
        return imagesEncode;
    }

    private getObjectWithDBData(data) {
        let index = 0;
        this.objectList =  new Array<ResponseObject>();
        for (let i = 0, objIndex = 0, trafficID = 0; i < data.length;  ++objIndex) {
            this.objectList[objIndex] = new ResponseObject();
            this.objectList[objIndex].cameras = new Array<string>();
            this.objectList[objIndex].lastDetectedDate = new Array<string>();
            this.objectList[objIndex].images = new Array<string>();
            this.objectList[objIndex].imagesEncode = new Array<string>();

            this.objectList[objIndex].type = data[i].type;
            this.objectList[objIndex].firstDetectedDate = data[i].first_detected_date;
            this.objectList[objIndex].id = data[i].traffic_id;

            index = 0;
            trafficID = data[i].traffic_id;

            while (i < data.length && trafficID === data[i].traffic_id ) {
                this.objectList[objIndex].cameras[index] = data[i].camera_name;
                this.objectList[objIndex].lastDetectedDate[index] = data[i].last_detected_date;
                this.objectList[objIndex].images = this.imagesNames(data[i].url);
                this.objectList[objIndex].imagesEncode =
                    this.objectList[objIndex].imagesEncode.concat(this.imagesEncode(this.objectList[objIndex].images));
                ++i;
                ++index;
            }
        }
    }

}
