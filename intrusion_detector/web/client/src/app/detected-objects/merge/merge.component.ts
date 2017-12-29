import { Component, OnInit,  Input, EventEmitter, Output, ViewChild } from '@angular/core';
import {  AuthenticationService } from '../../login/login.service';
import { ObjectService } from '../detected-object.service';
import { ErrorModalComponent } from '../../error-modal/error-modal.component';

@Component({
  selector: 'id-merge',
  templateUrl: './merge.component.html',
  styleUrls: ['./merge.component.css']
})

export class MergeComponent implements OnInit {
  @Input() selectedObjects;
  @Output() reload = new EventEmitter<boolean>();
  @ViewChild(ErrorModalComponent)
  modalHtml: ErrorModalComponent;

  constructor(private objectService: ObjectService, private authenticationService: AuthenticationService) {}

  ngOnInit() {
  }

  removeObject(object: any) {
    const index = this.selectedObjects.indexOf(object, 0);
      if (index > -1) {
          this.selectedObjects[index].isChecked = false;
          this.selectedObjects.splice(index, 1);
     }
  }

  private mergeObjects() {
    const mergeId: Array<Number> = new Array<Number>();
    for (let i = 0; i < this.selectedObjects.length; ++i) {
        mergeId.push(this.selectedObjects[i].id);
    }
      console.log(mergeId);
    this.objectService.mergeObjects(mergeId)
      .subscribe( data => {
        this.reload.emit(true);
          console.log(data);
        },
        error => {
            this.modalHtml.open(error);
        }
      );
  }
}
