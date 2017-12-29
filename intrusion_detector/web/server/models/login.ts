import { NextFunction, Request, Response } from "express";
import * as moment from "moment";
import * as uuidv5 from "uuid/v5";
import { token } from "../token";
import { db } from "./../models/database";

import { serverLogger } from "../utils/logger";

class Login {

    public static isUserExistOnDB(id) {
        return db.any("select * from users where users.id = $1", [id]);
    }

    constructor() {
        // empty
    }

    public login(req: Request, res: Response) {
        const MY_NAMESPACE = "1b671a64-40d5-491e-99b0-da01ff1f3341";
        const hashedPass = uuidv5(req.body.password, MY_NAMESPACE);
        // 3ca6ada1-90f3-54a6-85f0-0696a9974fc0 - pass in db
        db.any("select id from users where users.email = $1 and users.password = $2", [req.body.userName, hashedPass])
            .then((userID) => {
                const createdToken = token.createToken(userID[0].id); // create token with id
                serverLogger.logger.info(createdToken);
                serverLogger.logger.info(userID[0]);
                res.cookie("access_token", createdToken).status(200).json({
                    status: "User authenticated...",
                    success: true,
                    token: "token created",
                });
            })
            .catch ((err) => {
                res.status(401).json({
                    status: "User NOT authenticated...",
                    success: false,
                });
            },
        );
    }

    public isLogin(req: Request, res: Response, callback: (req: Request, res: Response) => any) {
        const reqToken = req.cookies.access_token;
        if (!(reqToken)) {
            return res.status(401).json({
                message: "No authentication token was provided in the request",
                success: false,
            });
        } else {
            token.verifyToken(reqToken, (err, decoded) => {
                if (err) {
                    return res.status(401).json({
                        status: "The access token used in the request is incorrect",
                        success: false,
                    });
                } else {
                    const now = moment().unix();
                    req.cookies.decodedToken = decoded;
                    if (now > decoded.exp) {
                        return res.status(401).json({
                            status: "The access token used in the request has expired",
                            success: false,
                        });
                    } else {
                        Login.isUserExistOnDB(decoded.id)
                            .then((data) => {
                                    callback(req, res);
                            })
                            .catch((newErr) => {
                                return res.status(401).json({
                                    status: "Incorect token",
                                    success: false,
                                });
                            });
                    }
                }
            });
        }
    }

}

export let login = new Login();
