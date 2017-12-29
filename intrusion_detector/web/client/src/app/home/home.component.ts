import { Component, OnInit } from '@angular/core';
import { CarouselConfig } from 'ngx-bootstrap/carousel';

@Component({
  selector: 'id-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  providers: [{provide: CarouselConfig, useValue: {interval: 5000, noPause: true}}]
})

export class HomeComponent implements OnInit {

  images= ['assets/images/home/detect1.jpg',
          'assets/images/home/detect2.jpg',
          'assets/images/home/detect3.jpg',
          'assets/images/home/detect4.jpg',
          'assets/images/home/detect5.jpg',
          'assets/images/home/detect6.jpg', ]

  constructor() { }

  ngOnInit() {
  }
}
