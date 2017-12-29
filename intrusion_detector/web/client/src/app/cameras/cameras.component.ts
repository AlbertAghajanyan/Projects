import { ViewContainerRef, ViewChild, ComponentFactoryResolver } from '@angular/core';
import { CookieService } from 'ngx-cookie';
import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../login/login.service';
import { Http, Response, Headers, RequestOptions, } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { CamerasService } from './cameras.service';
import { ErrorModalComponent } from '../error-modal/error-modal.component';

@Component({
  selector: 'id-cameras',
  templateUrl: './cameras.component.html',
  styleUrls: ['./cameras.component.css']
})

export class CamerasComponent implements OnInit {
  public search = {
    searchData : '',
    searchDataLat : '',
    searchDataLon : ''
  }
  public searchBy = 'camName';
  public selectData = [
    'Search by',
    'Name',
    'Location'
  ];
  // for ng-x pagination
  p = 1;
  private allItems: any[];
  // for search camera_name or location
  private nameOrLoc = '';
  // Edit are fields
  public editAreaName = '';
  public editAreaLatitude = '';
  public editAreaLongitude = '';
  public editAreaDescription = '';
  public editAreaOnline = true;
  // camera field
  private cam: any;

  // for not found message
  private notFound = false;
  @ViewChild(ErrorModalComponent)
  modalHtml: ErrorModalComponent;

  constructor(private http: Http, private camerasService: CamerasService,
    private authenticationService: AuthenticationService, private _cookieService: CookieService) {
  }

  ngOnInit() {
    this.getCameras();
  }

   /**
   * Get cameras list
   */
  getCameras() {
   this.camerasService.getCameras().subscribe(
      data => {
        this.allItems = data;
        this.allItems.forEach(element => {
          element.show = true;
        });
      },
      error => {
        console.log(error);
        this.modalHtml.open(error);
      },
        () => console.log('Done')
    );
  }

  /**
   * Show edit area
   *
   * @param cam camera field
   */
  show(cam) {
    this.cam = cam;
    this.editAreaName = cam.camera_name;
    this.editAreaLatitude = cam.location.x;
    this.editAreaLongitude = cam.location.y;
    this.editAreaDescription = cam.description;
    this.editAreaOnline = cam.online;
  }

  /**
   * Delete camera by id
   *
   * @param cam camera field
   */
  delete(cam) {
    this.camerasService.delCamera(cam.id).subscribe(
        data => {
            console.log(data);
            cam.show = false;
        },
        error => {
          console.log(error);
          this.modalHtml.open(error);
        },
        () => console.log('Done')
    );
  }

  /**
   * Called when clicked search button in cameras page
   *
   */
  formSubmit() {
    if (this.search.searchData) {
      // this.nameOrLoc = 'name=' + this.search.searchData;
    } else {
      // this.nameOrLoc = 'lat=' + this.search.searchDataLat + '&' + 'long=' + this.search.searchDataLon;
    }
    // this.searchCameras(this.nameOrLoc);
  }

   /**
   * Search cameras
   *
   * @param param camera_name or latitude/ longitude
   */
  /*
  searchCameras(param) {
    console.log('search cameras called');
    this.camerasService.searchCameras(param).subscribe(
      data => {
        // this.allItems = data;
         console.log("bla " + data);
      },
        error => {
          console.log(error);
          this.notFound = true;
          this.modalHtml.open(error);
        },
        () => console.log('Done')
    );
  }
  */

  /**
   * Clear search area
   */
  clearSearchArea() {
    this.search.searchData = '';
    this.search.searchDataLat = '';
    this.search.searchDataLon = '';
  }
}
