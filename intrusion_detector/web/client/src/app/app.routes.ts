import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { CamerasComponent } from './cameras/cameras.component';
import { LoginComponent } from './login/login.component';
import { DetectedObjectsComponent } from './detected-objects/detected-objects.component';

export const appRoutes: Routes = [
  { path: 'home', component: HomeComponent},
  { path: 'cameras', component: CamerasComponent},
  { path: 'detected', component: DetectedObjectsComponent },
  { path: 'login', component: LoginComponent},
  { path: '',   redirectTo: 'home', pathMatch: 'full' },
  { path: '*',   redirectTo: 'home', pathMatch: 'full' },
];
