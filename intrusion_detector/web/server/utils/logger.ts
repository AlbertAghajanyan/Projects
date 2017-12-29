import * as fs from "fs";
import { Logger, LoggerInstance, transports} from "winston";
import { configManager } from "../utils/configManager";

export class ServerLogger {
  public logger: LoggerInstance;
  private logDir = (process.env.NODE_ENV === "development" ? "server-dev" : "server-prod");

  constructor() {
    this.createFolder();
    const date = new Date();
    this.logger = new (Logger)({
      transports: [
        // colorize the output to the console
        new (transports.Console)({
          colorize: true,
          level: configManager.getTransportLevel(),
        }),

        new (transports.File)({
          datePattern: "yyyy-MM-dd.",
          // filename: this.logDir + "/server.log",
          filename: this.logDir + "/server_" + date + ".log",
          level: configManager.getTransportLevel(),
          prepend: true,
        }),
      ],
    });
  }

  public getLoggerObject() {
    return this.logger;
  }
  private createFolder() {
    if (!fs.existsSync(this.logDir)) {
      fs.mkdirSync(this.logDir);
    }
  }
}

const serverLogger: ServerLogger = new ServerLogger();
export { serverLogger };
