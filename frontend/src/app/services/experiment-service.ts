import { HttpClient, httpResource } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Experiment } from '../models/experiment';

// Relative to <base href>, so it resolves to /protein-screening/api
const API_URL = 'api';

@Injectable({ providedIn: 'root' })
export class ExperimentService {
  private readonly http = inject(HttpClient);

  /** All experiments, newest first. Reloaded whenever an experiment is created. */
  readonly experiments = httpResource<Experiment[]>(() => API_URL, { defaultValue: [] });

  create(name: string, sequences: string[]): Observable<Experiment> {
    return this.http
      .post<Experiment>(API_URL, { name, sequences })
      .pipe(tap(() => this.experiments.reload()));
  }

  createFromFasta(name: string, file: File): Observable<Experiment> {
    const body = new FormData();
    body.append('name', name);
    body.append('file', file);
    return this.http.post<Experiment>(API_URL, body).pipe(tap(() => this.experiments.reload()));
  }
}
