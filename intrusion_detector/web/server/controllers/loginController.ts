import { NextFunction, Request, Response } from "express";
import { login } from "./../models/login";
import { serverLogger } from "./../utils/logger";
class LoginController {

    constructor() {
        // empty
    }
    public login(req: Request, res: Response, next: NextFunction) {
        login.login(req, res);
    }

    public isLogin(req: Request, res: Response, next: NextFunction) {
        login.isLogin(req, res, next);
    }
}

const loginController = new LoginController();

export { loginController };
