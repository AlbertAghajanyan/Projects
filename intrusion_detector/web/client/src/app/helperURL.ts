import { environment } from './../environments/environment';

class HelperURL {
    private URL;
    constructor() {
        this.URL = '/api/v1'
    }

    public getURL(): string {
        return this.URL;
    }
}

const helperURL = new HelperURL();
export { helperURL };
