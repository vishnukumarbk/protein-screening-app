import { Component } from '@angular/core';
import { ExperimentList } from '../../components/experiment-list/experiment-list';

@Component({
  imports: [ExperimentList],
  selector: 'app-experiments',
  styleUrl: './experiments.scss',
  templateUrl: './experiments.html',
})
export class Experiments {}
