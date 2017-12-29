import * as fs from "fs";

class ConfigManager {
    private object;
    public constructor() {
        this.object = this.getConfigObject();
    }

    public getDatabaseConfig() {
        if (this.isProd()) {
            return this.object.production.db;
        }
        return this.object.development.db;
    }

    public getTransportLevel() {
        if (this.isProd()) {
            return this.object.production.transportLevel;
        }
        return this.object.development.transportLevel;
    }

    private isProd() {
        return process.env.NODE_ENV === "production";
    }

    private getConfigObject() {
        return JSON.parse(fs.readFileSync("./server/config.json", "utf8"));
    }
}

const configManager = new ConfigManager();
export { configManager };
