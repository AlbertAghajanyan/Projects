import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { } from 'jasmine';
import { DtEditAreaComponent } from './edit-area.component';

xdescribe('DtEditAreaComponent', () => {
  let component: DtEditAreaComponent;
  let fixture: ComponentFixture<DtEditAreaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DtEditAreaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DtEditAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
