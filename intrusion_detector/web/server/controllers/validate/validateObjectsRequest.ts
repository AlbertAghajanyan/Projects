import {NextFunction, Request, Response} from "express";
import {APP_RESPONSES} from "./../../appResponse";

export class ValidateObjectRequest {

    constructor() {
        // empty
    }

    public checkGetObjectById(req: Request, res: Response, next: NextFunction) {
        return this.checkId(req, res, next);
    }

    public checkEditObjectById(req: Request, res: Response, next: NextFunction) {
        if (this.checkId(req, res, next)) {
           return this.checkEditBody(req, res, next);
        }
        return false;
    }

    public checkDeleteObjectById(req: Request, res: Response, next: NextFunction) {
        return this.checkId(req, res, next);
    }

    public checkMargeObjects(req: Request, res: Response, next: NextFunction) {
        return true;
    }

    private checkEditBody(req: Request, res: Response, next: NextFunction) {
        const type = req.body.type;
        const firstDetectedDate = req.body.first_detected_date;

        const status = APP_RESPONSES.development.incorrect_body_param.httpCode;
        const message = APP_RESPONSES.development.incorrect_body_param.body.message;

        if (type === undefined || firstDetectedDate === undefined) {
                res.status(status).json({error: message});
                return false;
            }

        if (isNaN(Date.parse(req.body.first_detected_date))) {
                res.status(status).json({error: message});
                return false;
            }

        if (type !== "people" && type !== "car") {
                res.status(status).json({error: message});
                return false;
            }
        return true;
    }

    private checkId(req: Request, res: Response, next: NextFunction) {
        const status = APP_RESPONSES.development.incorrect_object_id.httpCode;
        const message = APP_RESPONSES.development.incorrect_object_id.body.message;
        if (req.params.id < 1 || !Number.isInteger(Number(req.params.id))) {
            res.status(status).json({error: message});
            return false;
        }
        req.params.id = Number.parseInt(req.params.id);
        return true;
    }

    private checkQueryParams(req: Request, res: Response, next: NextFunction) {
        const status = APP_RESPONSES.development.incorrect_object_id.httpCode;
        const message = APP_RESPONSES.development.incorrect_object_id.body.message;
        let first = req.query.first;
        let second = req.query.second;
        if (first < 1 || !Number.isInteger(Number(first))
             || second < 1 || !Number.isInteger(Number(second)) ) {
            res.status(status).json({error: message});
            return false;
        }
        first = Number.parseInt(first);
        second = Number.parseInt(second);
        return true;
    }
}
