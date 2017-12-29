import * as fs from "fs";
import * as jToken from "jsonwebtoken";
import * as moment from "moment";
import * as uuid from "uuid";

class Token {

    private secretKey: string;

    constructor() {
        this.secretKey = uuid.v4();
        // write to file secret key
        /*fs.writeFile("./server/secretKey", this._secretKey, function(err) {
            if(err) {
                return console.log(err);
            }
            console.log("The secret key was saved on file!");
        }); */
    }

    public createToken(id: number): string {

        const claims = {
            exp: moment().add(14, "days").unix(), // token lives time
            iat: moment().unix(), // token generation time
            iss: "https://intrusionDetector.am",
            sub: id, // DB user id
        };

        return jToken.sign(claims, this.secretKey);
    }

    public verifyToken(token: string, callback: any): any {
        return jToken.verify(token, this.secretKey, callback);
    }

}

const token = new Token();

export { token };
