import { Component } from '@angular/core';
import { ExperimentForm } from '../../components/experiment-form/experiment-form';

@Component({
  imports: [ExperimentForm],
  selector: 'app-home',
  styleUrl: './home.scss',
  templateUrl: './home.html',
})
export class Home {}
