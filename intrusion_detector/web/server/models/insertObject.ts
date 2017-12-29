import { NextFunction, Request, Response } from "express";
import * as fs from "fs";
import * as multer from "multer";
import { APP_RESPONSES } from "../appResponse";
import { db } from "./../models/database";

import { serverLogger } from "../utils/logger";

export class NewObject {
    public static imagesFolder = "./server/upload/images/";
    public type;
    public cameraName;
    public detectedDate;
    public folderName;

    // response codes
    private notFound: any;
    private internalErr: any;
    private internalDBErr: any;
    private incorrectParam: any;
    private success: any;
    private created: any;

    constructor() {
        this.type = "";
        this.detectedDate = "";
        this.cameraName = "";
        this.folderName = "folder";
        if (process.env.NODE_ENV === "development") {
            this.notFound = APP_RESPONSES.development.not_found;
            this.internalErr = APP_RESPONSES.development.internal_error;
            this.internalDBErr = APP_RESPONSES.development.internal_dbs_error;
            this.incorrectParam = APP_RESPONSES.development.incorrect_param;
            this.success = APP_RESPONSES.development.ok;
            this.created = APP_RESPONSES.development.created;
        } else {
            this.notFound = APP_RESPONSES.production.not_found;
            this.internalErr = APP_RESPONSES.production.internal_error;
            this.internalDBErr = APP_RESPONSES.production.internal_dbs_error;
            this.incorrectParam = APP_RESPONSES.production.incorrect_param;
            this.success = APP_RESPONSES.production.ok;
            this.created = APP_RESPONSES.production.created;
        }
    }

    public insertObjectOnDB() {
        return db.any("select count(*) from image")
            .then((data) => {
                this.folderName = this.folderName + (++data[0].count);
                return db.any("insert into traffic(type, first_detected_date) values($1,  to_timestamp\
                    (" + this.detectedDate + ") ) returning id", [this.type])
                    .then((dataInsertTraffic) => {
                        const traficId = dataInsertTraffic[0].id;
                        return db.any("select id from cameras where cameras.camera_name = $1", this.cameraName)
                            .then((dataSelectId) => {
                                const cameraId = dataSelectId[0].id;
                                return db.any("insert into image(url, last_detected_date, cameras_id, traffic_id) \
                                    values($1, '2017-01-01 12:03:00'::timestamp, $2, $3)",
                                    [NewObject.imagesFolder + this.folderName, cameraId, traficId])
                                    .then((dataInsertImage) => {
                                        this.createFolderWithObject(this.folderName);
                                    });
                            });
                    });
            });
    }

    public insertObject(req: Request, res: Response) {
        this.type = req.body.type;
        this.detectedDate = req.body.firstDetectedDate;
        this.cameraName = req.body.cameraName;
        if (this.isValid()) {
            this.insertObjectOnDB()
            .then((data) => {
                res.status(this.created.httpCode).json({
                    code: this.created.body.code,
                    folderName: this.folderName,
                });
            })
            .catch((err) => {
                res.status(this.internalDBErr.httpCode).json(this.internalDBErr.body);
            });
        } else {
            res.status(this.incorrectParam.httpCode).json(this.incorrectParam.body);
        }
    }

    public insertObjectImage(req: Request, res: Response) {
        if (!req.files || !req.query.folderName) {
            this.removeImages(req);
            res.status(this.incorrectParam.httpCode).json(this.incorrectParam.body);
        }  else {
            if (this.moveImages(req) === true) {
                res.status(this.created.httpCode).json(this.created.body) ;
            } else {
                res.status(this.incorrectParam.httpCode).json(this.incorrectParam.body);
            }
        }
    }

    public uploadImage() {
        const storage = multer.diskStorage({
            destination: (req, file, cb) => {
                cb(null, "./server/upload/images");
            },
            filename: (req, file, cb) => {
                cb(null, file.originalname);
            },
        });
        return multer({ storage});
    }

    private isValid(): boolean {
        if (this.detectedDate && this.cameraName && this.type) {
            return true;
        }
        return false;
    }

    private createFolderWithObject(folderName) {
        if (!fs.existsSync(NewObject.imagesFolder + folderName)) {
            fs.mkdirSync(NewObject.imagesFolder + folderName);
        }
    }

    private moveImages(req: Request): boolean {
        let name = [].concat(req.files)[0].originalname;
        if (fs.existsSync(NewObject.imagesFolder  + req.query.folderName)) {
            for (const item of [].concat(req.files)) {
                name = item.originalname;
                fs.renameSync(NewObject.imagesFolder + name, NewObject.imagesFolder  +
                    req.query.folderName + "/" + name);
            }
            return true;
        }
        this.removeImages(req);
        return false;
    }

    private removeImages(req: Request) {
        if ([].concat(req.files).length) {
            for (const item of [].concat(req.files)) {
                fs.unlink((NewObject.imagesFolder + item.originalname), (err) => {
                    serverLogger.logger.error("Could not find the folder name.Image deleted.");
                });
            }
        }  
    }

}
