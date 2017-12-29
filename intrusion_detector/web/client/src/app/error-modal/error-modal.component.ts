import { Component, OnInit, ViewChild } from '@angular/core';
import { Overlay, overlayConfigFactory } from 'ngx-modialog';
import { Modal, BSModalContext } from 'ngx-modialog/plugins/bootstrap';

@Component({
  selector: 'id-error-modal',
  templateUrl: './error-modal.component.html',
  styleUrls: ['./error-modal.component.css'],
  providers: [Modal]

})
export class ErrorModalComponent implements OnInit {
  public status;
  public statusText;
  public message;

  constructor(public modal: Modal) { }

  ngOnInit() {
  }

  open(err) {
    this.status = err.status;
    if (this.status === 0) {
      this.message = 'Server not found';
    } else {
      this.message = JSON.parse(err._body).status;
    }
    this.statusText = err.statusText;
    const errorContent = `
            <p><b>Error</b> - ` + this.status + `  ` +  this.statusText + `</p>
        <p><b>Message</b> - ` + this.message + `</p>`;
    this.modal.alert()
        .showClose(true)
        .title('Error')
        .body(errorContent)
        .open();
  }

}
