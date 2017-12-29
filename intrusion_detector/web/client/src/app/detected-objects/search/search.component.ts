import { Component, OnInit, Input, ViewChild, ElementRef, EventEmitter, Output  } from '@angular/core';
import { ObjectService } from '../detected-object.service';
import { Router } from '@angular/router';
import { ErrorModalComponent } from '../../error-modal/error-modal.component';

@Component({
  selector: 'id-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})

export class SearchComponent implements OnInit {
  @Input() objects;
  @Output() searching = new EventEmitter<boolean>();
  @Output() reload = new EventEmitter<boolean>();
  @ViewChild('myDate') myDate: ElementRef;
  @ViewChild('camName') camName: ElementRef;
  @ViewChild('objType') objType: ElementRef;
  @ViewChild('objTypeMobile') objTypeMobile: ElementRef;
  @ViewChild('camNameMobile') camNameMobile: ElementRef;
  @ViewChild('resetLabel') resetLabel: ElementRef;
  @ViewChild('resetMobileLabel') resetMobileLabel: ElementRef;
  @ViewChild(ErrorModalComponent)
  modalHtml: ErrorModalComponent;

  private cameraName: String;
  private objectType: String;
  private daterange: any = {};
  private isSearchbarOpen = true;
  private searchDisable = true;
  private dateRange: any;
  private isSearchEnable = true;
  public options: any = {
        timePicker: true,
        timePickerIncrement: 5,
        startDate: '2017-01-01 12:03:00',
        locale: {
            format: 'YYYY-MM-DD h:mm:ss'
        },
        alwaysShowCalendars: false,
  };

  constructor(private objectService: ObjectService, private  _router: Router) { }



  // private cameraName:any;
  // private objectType:any;
  ngOnInit() {
      this.resetLabel.nativeElement.hidden = true;
  }

  /**
   * Open navigation bar
   */
  private openNav() {
    document.getElementById('searchbar').style.width = '280px';
    this.isSearchbarOpen = false;
  }

  /**
   * Close navigation bar
   */
  private closeNav() {
    document.getElementById('searchbar').style.width = '0';
    this.isSearchbarOpen = true;
  }

  /**
   * Search object
   */
   private search() {
    let cameraName = this.cameraName;
    let objectType = this.objectType;
    const date = this.myDate.nativeElement.value.split(' - ');
    const startDate = (new Date(date[0]).getTime()) / 1000;
    const endDate = (new Date(date[1]).getTime()) / 1000;
    if ((cameraName === undefined || cameraName === '') && (objectType === undefined || objectType === '')) {
      cameraName =  undefined;
      objectType = undefined;
      this.reload.emit(true);
      return;
    }
    
    const body: Object = {'camera_name': cameraName, 'type': objectType, 'from_date': startDate, 'to_date': endDate};
    this.objectService.searchObjects(body)
    .subscribe(
          data => {
            this.resetLabel.nativeElement.hidden = false;
            this.resetMobileLabel.nativeElement.hidden = false;
            this.objects = this.objectService.objects.objects;
            this.searching.emit(this.objectService.objects.objects);
          },
          error => {
            this.modalHtml.open(error);
          },
      );
      this.closeNav();
  }

  /**
   * Reset Search data
   */
  private reset() {
    this.resetMobileLabel.nativeElement.hidden = true;
    this.resetLabel.nativeElement.hidden = true;
    this.camName.nativeElement.value = '';
    this.objType.nativeElement.value = '';
    this.camNameMobile.nativeElement.value = '';
    this.objTypeMobile.nativeElement.value = '';
    this.reload.emit(true);
    this.closeNav();
    this.dateRange = undefined;
    this.isSearchEnable = true;
  }

  /**
   * Get search date from date picker
   *
   * @param value date picker output object
   */
  private selectedDate(value: any) {
      this.daterange.start = value.start._d;
      this.daterange.end = value.end._d;
  }

  private change() {
    if (this.dateRange === undefined && this.cameraName === undefined && this.objectType === undefined) {
      this.isSearchEnable = true;
      return;
    } 
    if (this.dateRange === undefined && this.cameraName === '' && this.objectType === '') {
      this.isSearchEnable = true;
      return;
    } 
    this.isSearchEnable = false;
  }
}
