import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { CookieService } from 'ngx-cookie';
import { RequestOptions, Request, RequestMethod } from '@angular/http'
import { Router } from '@angular/router';
import { helperURL} from './../helperURL'

@Injectable()
export class AuthenticationService {

    public  isLogin = this. _cookieService.get('access_token') ? true : false;

    constructor(private  _router: Router, private http: Http, private _cookieService: CookieService) {
    };

    /**
     * Login to the site
     * @param username inputed username
     * @param password inputed password
     */
    login(username: string, password: string) {
        const options = new RequestOptions({ withCredentials: true, body: { userName: username, password: password } });
        return this.http.post(helperURL.getURL() + '/login', JSON.stringify({ userName: username, password: password }), options)
            .map((response: Response) => {
                // login successful if there's a jwt token in the response
                console.log(response.json());
                if (this. _cookieService.get('access_token')) {
                    this.isLogin = true;
                }
            });
    };

    /**
     * Logout from site
     */
    logout() {
        // remove user from local storage to log user
        this._cookieService.removeAll();
        this._router.navigate(['/login']);
        this.isLogin = false;
    }
}
