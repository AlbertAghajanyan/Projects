import * as bodyParser from "body-parser";
import * as compression from "compression";
import * as cookieParser from "cookie-parser";
import * as express from "express";
import { AppRouter } from "./routes/appRoutes";

import * as path from "path";

import { NextFunction, Request, Response } from "express";

class App {

    // ref to Express instance
    public express: express.Application;
    public appRouter: AppRouter;

    // Run configuration methods on the Express instance.
    constructor() {
        // create new token when app started
        this.appRouter = new AppRouter();
        this.express = express();
        this.middleware();
        this.routes();
        this.setupProd();
    }

    // Configure Express middleware.
    private middleware(): void {
        this.express.use(cookieParser("signed-with-secret-key"));
        this.express.use(compression());
        this.express.use(bodyParser.json());
        this.express.use(bodyParser.urlencoded({ extended: false }));
    }

    // Configure API endpoints.
    private routes(): void {
        this.accessControll();
        this.express.use("/api/v1/", this.appRouter.getRouter());
    }

    private  accessControll() {
        this.express.use((req, res, next) => {
            // Website you wish to allow to connect
            res.setHeader("Access-Control-Allow-Origin", "*");
            // Request methods you wish to allow
            res.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE");
            // Request headers you wish to allow
            res.setHeader("Access-Control-Allow-Headers", "X-Requested-With,content-type,Cookies");
            // Set to true if you need the website to include cookies in the requests sent
            // to the API (e.g. in case you use sessions)
            res.setHeader("Access-Control-Allow-Credentials", "true");
            // Pass to next layer of middleware

            next();
        });
    }

    private setupProd(): void {
        if (process.env.NODE_ENV !== "development") {
            this.express.set("views", path.join(__dirname, "views"));
            this.express.use(express.static(path.join(__dirname, "views")));
            this.express.get("*", (req, res) => {
                res.sendFile(path.join(__dirname, "views/index.html"));
            });
        }
    }
}

const  app = new App().express;
export { app };
