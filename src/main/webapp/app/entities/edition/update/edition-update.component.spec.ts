import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EditionService } from '../service/edition.service';
import { IEdition } from '../edition.model';
import { EditionFormService } from './edition-form.service';

import { EditionUpdateComponent } from './edition-update.component';

describe('Edition Management Update Component', () => {
  let comp: EditionUpdateComponent;
  let fixture: ComponentFixture<EditionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let editionFormService: EditionFormService;
  let editionService: EditionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EditionUpdateComponent],
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
      .overrideTemplate(EditionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EditionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    editionFormService = TestBed.inject(EditionFormService);
    editionService = TestBed.inject(EditionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const edition: IEdition = { id: 456 };

      activatedRoute.data = of({ edition });
      comp.ngOnInit();

      expect(comp.edition).toEqual(edition);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEdition>>();
      const edition = { id: 123 };
      jest.spyOn(editionFormService, 'getEdition').mockReturnValue(edition);
      jest.spyOn(editionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ edition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: edition }));
      saveSubject.complete();

      // THEN
      expect(editionFormService.getEdition).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(editionService.update).toHaveBeenCalledWith(expect.objectContaining(edition));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEdition>>();
      const edition = { id: 123 };
      jest.spyOn(editionFormService, 'getEdition').mockReturnValue({ id: null });
      jest.spyOn(editionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ edition: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: edition }));
      saveSubject.complete();

      // THEN
      expect(editionFormService.getEdition).toHaveBeenCalled();
      expect(editionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEdition>>();
      const edition = { id: 123 };
      jest.spyOn(editionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ edition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(editionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
