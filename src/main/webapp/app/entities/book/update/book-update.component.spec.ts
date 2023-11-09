import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAuthor } from 'app/entities/author/author.model';
import { AuthorService } from 'app/entities/author/service/author.service';
import { IEdition } from 'app/entities/edition/edition.model';
import { EditionService } from 'app/entities/edition/service/edition.service';
import { IBook } from '../book.model';
import { BookService } from '../service/book.service';
import { BookFormService } from './book-form.service';

import { BookUpdateComponent } from './book-update.component';

describe('Book Management Update Component', () => {
  let comp: BookUpdateComponent;
  let fixture: ComponentFixture<BookUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bookFormService: BookFormService;
  let bookService: BookService;
  let authorService: AuthorService;
  let editionService: EditionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), BookUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BookUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BookUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bookFormService = TestBed.inject(BookFormService);
    bookService = TestBed.inject(BookService);
    authorService = TestBed.inject(AuthorService);
    editionService = TestBed.inject(EditionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call author query and add missing value', () => {
      const book: IBook = { id: 456 };
      const author: IAuthor = { id: 18003 };
      book.author = author;

      const authorCollection: IAuthor[] = [{ id: 8472 }];
      jest.spyOn(authorService, 'query').mockReturnValue(of(new HttpResponse({ body: authorCollection })));
      const expectedCollection: IAuthor[] = [author, ...authorCollection];
      jest.spyOn(authorService, 'addAuthorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ book });
      comp.ngOnInit();

      expect(authorService.query).toHaveBeenCalled();
      expect(authorService.addAuthorToCollectionIfMissing).toHaveBeenCalledWith(authorCollection, author);
      expect(comp.authorsCollection).toEqual(expectedCollection);
    });

    it('Should call edition query and add missing value', () => {
      const book: IBook = { id: 456 };
      const edition: IEdition = { id: 4413 };
      book.edition = edition;

      const editionCollection: IEdition[] = [{ id: 27330 }];
      jest.spyOn(editionService, 'query').mockReturnValue(of(new HttpResponse({ body: editionCollection })));
      const expectedCollection: IEdition[] = [edition, ...editionCollection];
      jest.spyOn(editionService, 'addEditionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ book });
      comp.ngOnInit();

      expect(editionService.query).toHaveBeenCalled();
      expect(editionService.addEditionToCollectionIfMissing).toHaveBeenCalledWith(editionCollection, edition);
      expect(comp.editionsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const book: IBook = { id: 456 };
      const author: IAuthor = { id: 19730 };
      book.author = author;
      const edition: IEdition = { id: 28238 };
      book.edition = edition;

      activatedRoute.data = of({ book });
      comp.ngOnInit();

      expect(comp.authorsCollection).toContain(author);
      expect(comp.editionsCollection).toContain(edition);
      expect(comp.book).toEqual(book);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBook>>();
      const book = { id: 123 };
      jest.spyOn(bookFormService, 'getBook').mockReturnValue(book);
      jest.spyOn(bookService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ book });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: book }));
      saveSubject.complete();

      // THEN
      expect(bookFormService.getBook).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bookService.update).toHaveBeenCalledWith(expect.objectContaining(book));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBook>>();
      const book = { id: 123 };
      jest.spyOn(bookFormService, 'getBook').mockReturnValue({ id: null });
      jest.spyOn(bookService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ book: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: book }));
      saveSubject.complete();

      // THEN
      expect(bookFormService.getBook).toHaveBeenCalled();
      expect(bookService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBook>>();
      const book = { id: 123 };
      jest.spyOn(bookService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ book });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bookService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAuthor', () => {
      it('Should forward to authorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(authorService, 'compareAuthor');
        comp.compareAuthor(entity, entity2);
        expect(authorService.compareAuthor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEdition', () => {
      it('Should forward to editionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(editionService, 'compareEdition');
        comp.compareEdition(entity, entity2);
        expect(editionService.compareEdition).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
