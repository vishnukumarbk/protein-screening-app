import { DatePipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ExperimentService } from '../../services/experiment-service';

@Component({
  imports: [DatePipe],
  selector: 'app-experiment-list',
  styleUrl: './experiment-list.scss',
  templateUrl: './experiment-list.html',
})
export class ExperimentList {
  protected readonly experiments = inject(ExperimentService).experiments;
}
