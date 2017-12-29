import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { } from 'jasmine';
import { DetectedObjectsComponent } from './detected-objects.component';

xdescribe('DetectedObjectsComponent', () => {
  let component: DetectedObjectsComponent;
  let fixture: ComponentFixture<DetectedObjectsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetectedObjectsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetectedObjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
