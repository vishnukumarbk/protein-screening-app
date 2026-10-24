import { Routes } from '@angular/router';
import { Experiments } from './pages/experiments/experiments';
import { Home } from './pages/home/home';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'experiments', component: Experiments },
];
