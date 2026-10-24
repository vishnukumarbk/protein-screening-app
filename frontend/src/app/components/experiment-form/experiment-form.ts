import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, inject, signal, viewChild } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { Experiment } from '../../models/experiment';
import { ExperimentService } from '../../services/experiment-service';

const SEQUENCE_COUNT = 20;

// Sequences are separated by new lines or spaces, so pasted lists split correctly however they are laid out
function parseSequences(text: string): string[] {
  return text
    .split(/\s+/)
    .filter(Boolean)
    .map((sequence) => sequence.toUpperCase());
}

const exactSequenceCount: ValidatorFn = (control) =>
  parseSequences(control.value).length === SEQUENCE_COUNT ? null : { sequenceCount: true };

type SequenceSource = 'manual' | 'fasta';

@Component({
  imports: [ReactiveFormsModule, RouterLink],
  selector: 'app-experiment-form',
  styleUrl: './experiment-form.scss',
  templateUrl: './experiment-form.html',
})
export class ExperimentForm {
  private readonly experimentService = inject(ExperimentService);
  private readonly formBuilder = inject(FormBuilder).nonNullable;
  private readonly fileInput = viewChild<ElementRef<HTMLInputElement>>('fileInput');

  protected readonly sequenceCount = SEQUENCE_COUNT;
  protected readonly source = signal<SequenceSource>('manual');
  protected readonly file = signal<File | null>(null);
  protected readonly submitting = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly created = signal<Experiment | null>(null);

  protected readonly form = this.formBuilder.group({
    name: ['', [Validators.required, Validators.pattern(/\S/)]],
    sequences: ['', exactSequenceCount],
  });

  protected enteredCount(): number {
    return parseSequences(this.form.controls.sequences.value).length;
  }

  protected canSubmit(): boolean {
    const sequencesReady =
      this.source() === 'manual' ? this.form.controls.sequences.valid : this.file() !== null;
    return !this.submitting() && this.form.controls.name.valid && sequencesReady;
  }

  protected setSource(source: SequenceSource): void {
    this.source.set(source);
    this.file.set(null);
    this.error.set(null);
  }

  protected selectFile(event: Event): void {
    this.file.set((event.target as HTMLInputElement).files?.item(0) ?? null);
  }

  protected submit(): void {
    const file = this.file();
    if (!this.canSubmit()) {
      return;
    }
    const name = this.form.controls.name.value.trim();
    const request =
      this.source() === 'fasta' && file
        ? this.experimentService.createFromFasta(name, file)
        : this.experimentService.create(name, parseSequences(this.form.controls.sequences.value));

    this.submitting.set(true);
    this.error.set(null);
    this.created.set(null);
    request.pipe(finalize(() => this.submitting.set(false))).subscribe({
      next: (experiment) => {
        this.created.set(experiment);
        this.reset();
      },
      // The backend explains rejections in the problem detail, e.g. which sequence rule failed
      error: (error: HttpErrorResponse) =>
        this.error.set(
          error.error?.detail ?? 'The experiment could not be created. Please try again.',
        ),
    });
  }

  private reset(): void {
    this.form.reset();
    this.file.set(null);
    const input = this.fileInput()?.nativeElement;
    if (input) {
      input.value = '';
    }
  }
}
