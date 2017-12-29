import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Daterangepicker } from 'ng2-daterangepicker';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { HomeComponent, CamerasComponent, LoginComponent, DetectedObjectsComponent,
  CarouselComponent, NavigationBarComponent, appRoutes } from './app.index'
import { CarouselModule } from 'ngx-bootstrap/carousel';

import { HttpModule } from '@angular/http';
import { AuthenticationService } from './login/login.service';
import { ObjectService } from './detected-objects/detected-object.service'
import { CamerasService } from './cameras/cameras.service';

import { CookieModule } from 'ngx-cookie';

import { NgxPaginationModule } from 'ngx-pagination';
import { EditAreaComponent } from './cameras/edit-area/edit-area.component';
import { DtEditAreaComponent } from './detected-objects/edit-area/edit-area.component';
import { CamerasFilterPipe } from './cameras/cameras-filter.pipe';
import { SearchComponent } from './detected-objects/search/search.component';
import { MergeComponent } from './detected-objects/merge/merge.component';

import { DateTimePickerModule } from 'ng-pick-datetime';
import { ErrorModalComponent } from './error-modal/error-modal.component';
import { ModalModule } from 'ngx-modialog';
import { BootstrapModalModule } from 'ngx-modialog/plugins/bootstrap';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    CamerasComponent,
    LoginComponent,
    DetectedObjectsComponent,
    CarouselComponent,
    NavigationBarComponent,
    EditAreaComponent,
    DtEditAreaComponent,
    CamerasFilterPipe,
    SearchComponent,
    MergeComponent,
    ErrorModalComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule.forRoot(appRoutes),
    CookieModule.forRoot(),
    CarouselModule.forRoot(),
    Daterangepicker,
    HttpModule,
    NgxPaginationModule,
    DateTimePickerModule,
    ModalModule.forRoot(),
    BootstrapModalModule
  ],
  providers: [AuthenticationService, ObjectService, CamerasService],
  bootstrap: [AppComponent]
})
export class AppModule { }
