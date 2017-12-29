import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { ObjectService } from '../detected-object.service'
import { ErrorModalComponent } from '../../error-modal/error-modal.component';

@Component({
  selector: 'id-dt-edit-area',
  templateUrl: './edit-area.component.html',
  styleUrls: ['./edit-area.component.css']
})

export class DtEditAreaComponent implements OnInit {
  @Input() object;
  @Input() objType;
  @Input() date;
  @ViewChild(ErrorModalComponent)
  modalHtml: ErrorModalComponent;
  private momentVariable: any;

  constructor( private objectService: ObjectService) {
  }

  ngOnInit() {
  }

  /**
   * Get date from date picker
   *
   * @param moment date picker output
   */
  public setMoment(moment: any): any {
    this.momentVariable = moment;
    this.date = moment;
  }

  /**
   * Edit object information
   */
  public editObject() {
    if (this.date === '' || this.objType === '') {
        console.log('Empty name or descriptioin.');
      } else {
        this.objectService.editObject(this.object.id, this.objType, this.date).subscribe(
          data => {
              console.log(data);
              this.object.type = this.objType;
              this.object.firstDetectedDate = this.date;
          },
          error => {
            this.modalHtml.open(error);
          },
          () => console.log('Done')
        );
      }
    }

}
