import { NextFunction, Request, Response } from "express";
import { Cameras } from "./../models/cameras";
import { login } from "./../models/login";
import { ValidateCamerasRequest } from "./validate/validateCamerasRequest";

import { serverLogger } from "../utils/logger";

const cameras = new Cameras();
const validate = new ValidateCamerasRequest();

export class CameraController {
    constructor() {
        // empty
    }

    public getCameras(req: Request, res: Response, next: NextFunction) {
        serverLogger.logger.info("Called get all cameras");
        cameras.getCameras(req, res);
    }

    public getCameraById(req: Request, res: Response, next: NextFunction) {
        if (validate.checkGetCameraById(req, res, next)) {
            serverLogger.logger.info("Called get camera by id with id = " + req.params.id);
            cameras.getCameraById(req, res);
        }
    }

    public getCamerasWithRange(req: Request, res: Response, next: NextFunction) {
        res.send("Get Camera with range");
    }

    public deleteCameraById(req: Request, res: Response, next: NextFunction) {
        if (validate.checkDeleteCameraById(req, res, next)) {
            serverLogger.logger.info("Called delete camera by id with id = " + req.params.id);
            login.isLogin(req, res, cameras.deleteCameraById);
        }
    }

    public updateCameraById(req: Request, res: Response, next: NextFunction) {
        if (validate.checkUpdateCameraById(req, res, next)) {
            serverLogger.logger.info("Called edit camera by id with id = " + req.params.id);
            login.isLogin(req, res, cameras.updateCameraById);
        }
    }

}

const cameraController = new CameraController();
export default cameraController;
