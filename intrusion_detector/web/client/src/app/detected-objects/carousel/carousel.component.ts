import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'id-carousel',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.css']
})
export class CarouselComponent implements OnInit {
  @Input() object: any;
  @Output() onChanged = new EventEmitter<boolean>();
  // private isShow = true;
  constructor() { }

  ngOnInit() {
  }

  carouselNone() {
    this.onChanged.emit(false);
  }
}
