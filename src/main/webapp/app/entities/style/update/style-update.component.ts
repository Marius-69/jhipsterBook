import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBook } from 'app/entities/book/book.model';
import { BookService } from 'app/entities/book/service/book.service';
import { IStyle } from '../style.model';
import { StyleService } from '../service/style.service';
import { StyleFormService, StyleFormGroup } from './style-form.service';

@Component({
  standalone: true,
  selector: 'jhi-style-update',
  templateUrl: './style-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StyleUpdateComponent implements OnInit {
  isSaving = false;
  style: IStyle | null = null;

  booksSharedCollection: IBook[] = [];

  editForm: StyleFormGroup = this.styleFormService.createStyleFormGroup();

  constructor(
    protected styleService: StyleService,
    protected styleFormService: StyleFormService,
    protected bookService: BookService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareBook = (o1: IBook | null, o2: IBook | null): boolean => this.bookService.compareBook(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ style }) => {
      this.style = style;
      if (style) {
        this.updateForm(style);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const style = this.styleFormService.getStyle(this.editForm);
    if (style.id !== null) {
      this.subscribeToSaveResponse(this.styleService.update(style));
    } else {
      this.subscribeToSaveResponse(this.styleService.create(style));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStyle>>): void {
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

  protected updateForm(style: IStyle): void {
    this.style = style;
    this.styleFormService.resetForm(this.editForm, style);

    this.booksSharedCollection = this.bookService.addBookToCollectionIfMissing<IBook>(this.booksSharedCollection, style.book);
  }

  protected loadRelationshipsOptions(): void {
    this.bookService
      .query()
      .pipe(map((res: HttpResponse<IBook[]>) => res.body ?? []))
      .pipe(map((books: IBook[]) => this.bookService.addBookToCollectionIfMissing<IBook>(books, this.style?.book)))
      .subscribe((books: IBook[]) => (this.booksSharedCollection = books));
  }
}
