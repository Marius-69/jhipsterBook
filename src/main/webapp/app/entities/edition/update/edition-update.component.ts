import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEdition } from '../edition.model';
import { EditionService } from '../service/edition.service';
import { EditionFormService, EditionFormGroup } from './edition-form.service';

@Component({
  standalone: true,
  selector: 'jhi-edition-update',
  templateUrl: './edition-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EditionUpdateComponent implements OnInit {
  isSaving = false;
  edition: IEdition | null = null;

  editForm: EditionFormGroup = this.editionFormService.createEditionFormGroup();

  constructor(
    protected editionService: EditionService,
    protected editionFormService: EditionFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ edition }) => {
      this.edition = edition;
      if (edition) {
        this.updateForm(edition);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const edition = this.editionFormService.getEdition(this.editForm);
    if (edition.id !== null) {
      this.subscribeToSaveResponse(this.editionService.update(edition));
    } else {
      this.subscribeToSaveResponse(this.editionService.create(edition));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEdition>>): void {
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

  protected updateForm(edition: IEdition): void {
    this.edition = edition;
    this.editionFormService.resetForm(this.editForm, edition);
  }
}
