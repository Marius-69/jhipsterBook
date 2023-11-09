import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAuthor } from 'app/entities/author/author.model';
import { AuthorService } from 'app/entities/author/service/author.service';
import { IEdition } from 'app/entities/edition/edition.model';
import { EditionService } from 'app/entities/edition/service/edition.service';
import { BookService } from '../service/book.service';
import { IBook } from '../book.model';
import { BookFormService, BookFormGroup } from './book-form.service';

@Component({
  standalone: true,
  selector: 'jhi-book-update',
  templateUrl: './book-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BookUpdateComponent implements OnInit {
  isSaving = false;
  book: IBook | null = null;

  authorsCollection: IAuthor[] = [];
  editionsCollection: IEdition[] = [];

  editForm: BookFormGroup = this.bookFormService.createBookFormGroup();

  constructor(
    protected bookService: BookService,
    protected bookFormService: BookFormService,
    protected authorService: AuthorService,
    protected editionService: EditionService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAuthor = (o1: IAuthor | null, o2: IAuthor | null): boolean => this.authorService.compareAuthor(o1, o2);

  compareEdition = (o1: IEdition | null, o2: IEdition | null): boolean => this.editionService.compareEdition(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ book }) => {
      this.book = book;
      if (book) {
        this.updateForm(book);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const book = this.bookFormService.getBook(this.editForm);
    if (book.id !== null) {
      this.subscribeToSaveResponse(this.bookService.update(book));
    } else {
      this.subscribeToSaveResponse(this.bookService.create(book));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBook>>): void {
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

  protected updateForm(book: IBook): void {
    this.book = book;
    this.bookFormService.resetForm(this.editForm, book);

    this.authorsCollection = this.authorService.addAuthorToCollectionIfMissing<IAuthor>(this.authorsCollection, book.author);
    this.editionsCollection = this.editionService.addEditionToCollectionIfMissing<IEdition>(this.editionsCollection, book.edition);
  }

  protected loadRelationshipsOptions(): void {
    this.authorService
      .query({ filter: 'book-is-null' })
      .pipe(map((res: HttpResponse<IAuthor[]>) => res.body ?? []))
      .pipe(map((authors: IAuthor[]) => this.authorService.addAuthorToCollectionIfMissing<IAuthor>(authors, this.book?.author)))
      .subscribe((authors: IAuthor[]) => (this.authorsCollection = authors));

    this.editionService
      .query({ filter: 'book-is-null' })
      .pipe(map((res: HttpResponse<IEdition[]>) => res.body ?? []))
      .pipe(map((editions: IEdition[]) => this.editionService.addEditionToCollectionIfMissing<IEdition>(editions, this.book?.edition)))
      .subscribe((editions: IEdition[]) => (this.editionsCollection = editions));
  }
}
