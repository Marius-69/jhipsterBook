import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StyleDeuxService } from '../service/style-deux.service';
import { IStyleDeux } from '../style-deux.model';
import { StyleDeuxFormService } from './style-deux-form.service';

import { StyleDeuxUpdateComponent } from './style-deux-update.component';

describe('StyleDeux Management Update Component', () => {
  let comp: StyleDeuxUpdateComponent;
  let fixture: ComponentFixture<StyleDeuxUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let styleDeuxFormService: StyleDeuxFormService;
  let styleDeuxService: StyleDeuxService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), StyleDeuxUpdateComponent],
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
      .overrideTemplate(StyleDeuxUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StyleDeuxUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    styleDeuxFormService = TestBed.inject(StyleDeuxFormService);
    styleDeuxService = TestBed.inject(StyleDeuxService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const styleDeux: IStyleDeux = { id: 456 };

      activatedRoute.data = of({ styleDeux });
      comp.ngOnInit();

      expect(comp.styleDeux).toEqual(styleDeux);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStyleDeux>>();
      const styleDeux = { id: 123 };
      jest.spyOn(styleDeuxFormService, 'getStyleDeux').mockReturnValue(styleDeux);
      jest.spyOn(styleDeuxService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ styleDeux });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: styleDeux }));
      saveSubject.complete();

      // THEN
      expect(styleDeuxFormService.getStyleDeux).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(styleDeuxService.update).toHaveBeenCalledWith(expect.objectContaining(styleDeux));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStyleDeux>>();
      const styleDeux = { id: 123 };
      jest.spyOn(styleDeuxFormService, 'getStyleDeux').mockReturnValue({ id: null });
      jest.spyOn(styleDeuxService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ styleDeux: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: styleDeux }));
      saveSubject.complete();

      // THEN
      expect(styleDeuxFormService.getStyleDeux).toHaveBeenCalled();
      expect(styleDeuxService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStyleDeux>>();
      const styleDeux = { id: 123 };
      jest.spyOn(styleDeuxService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ styleDeux });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(styleDeuxService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
