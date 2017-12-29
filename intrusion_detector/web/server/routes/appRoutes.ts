import { Request, Router } from "express";
import * as multer from "multer";
import { CameraController } from "./../controllers/cameraController";
import { loginController } from "./../controllers/loginController";
import { ObjectControllers } from "./../controllers/objectController";

export class AppRouter {
    public router: Router;
    private cameraController: CameraController;
    private objectControllers: ObjectControllers;

    constructor() {
        this.cameraController = new CameraController();
        this.objectControllers = new ObjectControllers();
        this.router = Router();
        this.init();
    }

    public getRouter() {
        return this.router;
    }

    private init() {
        this.router.get("/cameras", this.cameraController.getCameras);
        this.router.get("/camera/:id", this.cameraController.getCameraById);
        this.router.delete("/camera/:id", this.cameraController.deleteCameraById);
        this.router.put("/camera/:id", this.cameraController.updateCameraById);

        this.router.get("/object/:id", this.objectControllers.getObjectById);
        this.router.get("/objects", this.objectControllers.getObjectList);
        this.router.delete("/object/:id", this.objectControllers.deleteObjectById);
        this.router.put("/object/:id", this.objectControllers.editObjectById);
        this.router.put("/objects", this.objectControllers.mergeObjects);
        this.router.get("/object", this.objectControllers.searchObject);

        this.router.post("/login", loginController.login);

        this.router.post("/object", this.objectControllers.insertObject);
        this.router.post("/objectImages",  this.objectControllers.uploadImages().array("images", 20),
            this.objectControllers.insertObjectImage);
    }

}
