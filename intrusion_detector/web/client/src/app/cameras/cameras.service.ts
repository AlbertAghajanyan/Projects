import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie';
import { Http, Response, Headers, RequestOptions, } from '@angular/http';
import { helperURL } from './../helperURL'


@Injectable()
export class CamerasService {

  constructor(private http: Http, private _cookieService: CookieService) { }

  /**
   * Send request for camera deleting
   *
   * @param id camera id
   */
  public delCamera(id) {
    const delUrl = helperURL.getURL() + '/camera/' + id;
    const headers = new Headers();
    headers.append('Cookies', this. _cookieService.get('access_token'));
    const options = new RequestOptions({ headers: headers, withCredentials: true });
    console.log(options);
    return this.http.delete(delUrl, options).map(res => res.json());
  }

  /**
   * Send request for camera editing
   *
   * @param id camera id
   * @param name camera name
   * @param latitude
   * @param longitude
   * @param description
   * @param online boolean value
   */
  public editCamera(id, name, latitude, longitude, description, online) {
    const delUrl = helperURL.getURL() + '/camera/' + id;
    const headers = new Headers();
    headers.append('Cookies', this. _cookieService.get('access_token'));
    headers.append('Content-Type', 'application/json');
    const options = new RequestOptions({ headers: headers, withCredentials: true });
    const json = {
      'cameraName': name,
      'latitude': latitude,
      'longitude': longitude,
      'description': description,
      'online': online
    }
    const body = JSON.stringify(json);
    console.log(body);
    return this.http.put(delUrl, body, options).map(res => res.json());
  }

  /**
   * Get camera list.
   */
  public getCameras() {
    console.log('get called');
    const getUrl = helperURL.getURL() + '/cameras';
    return this.http.get(getUrl).map(res => res.json());
  }


  /**
   * Send request for camera searching
   *
   * @param param searching parametres
   */
  /*
  public searchCameras(param) {
      console.log('searchCamera called');
      console.log(param);
      const searchUrl = helperURL.getURL() + '/cameraBy?' + param;
      return this.http.get(searchUrl).map(res => res.json());
  }
*/
}
