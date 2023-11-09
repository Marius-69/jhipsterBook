import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStyleDeux } from '../style-deux.model';
import { StyleDeuxService } from '../service/style-deux.service';
import { StyleDeuxFormService, StyleDeuxFormGroup } from './style-deux-form.service';

@Component({
  standalone: true,
  selector: 'jhi-style-deux-update',
  templateUrl: './style-deux-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StyleDeuxUpdateComponent implements OnInit {
  isSaving = false;
  styleDeux: IStyleDeux | null = null;

  editForm: StyleDeuxFormGroup = this.styleDeuxFormService.createStyleDeuxFormGroup();

  constructor(
    protected styleDeuxService: StyleDeuxService,
    protected styleDeuxFormService: StyleDeuxFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ styleDeux }) => {
      this.styleDeux = styleDeux;
      if (styleDeux) {
        this.updateForm(styleDeux);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const styleDeux = this.styleDeuxFormService.getStyleDeux(this.editForm);
    if (styleDeux.id !== null) {
      this.subscribeToSaveResponse(this.styleDeuxService.update(styleDeux));
    } else {
      this.subscribeToSaveResponse(this.styleDeuxService.create(styleDeux));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStyleDeux>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(styleDeux: IStyleDeux): void {
    this.styleDeux = styleDeux;
    this.styleDeuxFormService.resetForm(this.editForm, styleDeux);
  }
}
