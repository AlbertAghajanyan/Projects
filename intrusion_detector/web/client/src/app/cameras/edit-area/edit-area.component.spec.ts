import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { } from 'jasmine';
import { EditAreaComponent } from './edit-area.component';

xdescribe('EditAreaComponent', () => {
  let component: EditAreaComponent;
  let fixture: ComponentFixture<EditAreaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditAreaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
