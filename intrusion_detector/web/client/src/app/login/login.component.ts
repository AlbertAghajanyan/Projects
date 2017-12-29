import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from './login.service';
import { ErrorModalComponent } from '../error-modal/error-modal.component';

@Component({
  selector: 'id-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  login = {
    mail : '',
    password : '',
    rememberMe : false
  }
  @ViewChild(ErrorModalComponent)
  modalHtml: ErrorModalComponent;

  constructor(private  _router: Router, private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
  }

  /**
   * Try to login
   *
   * @param mailError email field error
   * @param passwordError  password field error
   */
  formSubmit(mailError, passwordError) {
    if (mailError === null ) {
      this.authenticationService.login(this.login.mail, this.login.password)
        .subscribe(
          data => {
            this._router.navigate(['home']);
            console.log('OK');
          },
          error => {
            console.log('Who are you??' + error);
            this.modalHtml.open(error);
          },
      );
    }
  }
}
