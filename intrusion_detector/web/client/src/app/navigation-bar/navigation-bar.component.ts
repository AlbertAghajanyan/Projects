import { Component, OnInit } from '@angular/core';
import {  AuthenticationService } from '../login/login.service';


@Component({
  selector: 'id-header',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.css']
})

export class NavigationBarComponent implements OnInit {

  constructor(private authenticationService: AuthenticationService) { }

  ngOnInit() {
  }

  /**
   * Login or logout
   */
  private isLogin() {
    if (this.authenticationService.isLogin === true) {
      this.authenticationService.logout();
    }
  }

}
