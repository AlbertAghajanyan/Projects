import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { CookieService } from 'ngx-cookie';

import 'rxjs/add/operator/map'
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw'

import { RequestOptions, Request, RequestMethod } from '@angular/http'
import { Router } from '@angular/router';
import { helperURL } from './../helperURL'

@Injectable()
export class ObjectService {
    public objects;
    constructor(private http: Http,  private cookieService: CookieService) {}

    public getObjectList(start: number, size: number): Observable<Response> {
        return this.http.get(helperURL.getURL() + '/objects?' + 'start=' + start + '&size=' + size)
            .map((response: Response) => {
                this.objects = response.json();
            })
            .catch(error => {
                if (error.status === 404) {
                    console.log('404 + ' + error);
                } else if (error.status === 500) {
                    console.log('500' + error);
                }
                return Observable.throw(error);
            })
    }

    public deleteObject(id) {
        const delUrl = helperURL.getURL() +  '/object/' + id;
        const headers = new Headers();
        headers.append('Cookies', this.cookieService.get('access_token'));
        const options = new RequestOptions({ headers: headers, withCredentials: true });
        return this.http.delete(delUrl, options).map(res => res.json());
    }

    public mergeObjects(objects) {
        const mergeUrl = helperURL.getURL() + '/objects';
        const headers = new Headers();
        headers.append('Cookies', this.cookieService.get('access_token'));
        const options = new RequestOptions({ headers: headers, withCredentials: true, body: {object: objects} });
        return this.http.put(mergeUrl,  JSON.stringify({mergedObjects: objects}), options)
        .map(res => res.json())
        .catch(error => {
                if (error.status === 404) {
                    console.log('404 + ' + error);
                } else if (error.status === 500) {
                    console.log('500' + error);
                }
                return Observable.throw(error);
            })
    }

    public searchObjects(body) {
        const headers = new Headers();
        return this.http.get(helperURL.getURL() + '/object' + '?type=' + body.type + '&camera_name=' + body.camera_name +
        '&from_date=' + body.from_date + '&to_date=' + body.to_date )
            .map((response: Response) => {
                this.objects = response.json();
                console.log(this.objects);
                return this.objects;
            })
            .catch(error => {
                if (error.status === 404) {
                    console.log('404 + ' + error);
                    this.objects = {};
                    alert('No data');
                    return this.objects;

                } else if (error.status === 500) {
                    console.log('500' + error);
                }
                return Observable.throw(error);
            })
    }

    public editObject(id, objType, date) {
        const editUrl = helperURL.getURL() + '/object/' + id;
        const headers = new Headers();
        headers.append('Cookies', this.cookieService.get('access_token'));
        headers.append('Content-Type', 'application/json');
        const options = new RequestOptions({ headers: headers, withCredentials: true });
        const json = {
            'type': objType,
            'first_detected_date' : date
        }
        const body = JSON.stringify(json);
        return this.http.put(editUrl, body, options).map(res => res.json());
    }
}
