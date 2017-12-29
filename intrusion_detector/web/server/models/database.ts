import { IDatabase, IMain } from "pg-promise";
import * as pgPromise from "pg-promise";
import { configManager } from "./../utils/configManager";

import { serverLogger } from "../utils/logger";

export class DataBase {
    private db: IDatabase<any>;

    constructor() {
        serverLogger.logger.info("DataBase constructor called");
        this.initDevOrProd();
    }

    public getDB() {
        return this.db;
    }

    private initDevOrProd() {
        const pgp: IMain = pgPromise ({  });
        this.db = pgp(configManager.getDatabaseConfig());
        this.db.connect()
            .then((data) => {
                serverLogger.logger.info("Data Base connection success ...");
            })
            .catch((err) => {
                serverLogger.logger.info("Data base connection failure !!!");
            });
    }

}

const database: any = new DataBase();
export let db = database.getDB();
