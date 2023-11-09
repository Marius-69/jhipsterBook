import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../edition.test-samples';

import { EditionFormService } from './edition-form.service';

describe('Edition Form Service', () => {
  let service: EditionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EditionFormService);
  });

  describe('Service methods', () => {
    describe('createEditionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEditionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dayOfPublication: expect.any(Object),
            monthOfPublication: expect.any(Object),
          }),
        );
      });

      it('passing IEdition should create a new form with FormGroup', () => {
        const formGroup = service.createEditionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dayOfPublication: expect.any(Object),
            monthOfPublication: expect.any(Object),
          }),
        );
      });
    });

    describe('getEdition', () => {
      it('should return NewEdition for default Edition initial value', () => {
        const formGroup = service.createEditionFormGroup(sampleWithNewData);

        const edition = service.getEdition(formGroup) as any;

        expect(edition).toMatchObject(sampleWithNewData);
      });

      it('should return NewEdition for empty Edition initial value', () => {
        const formGroup = service.createEditionFormGroup();

        const edition = service.getEdition(formGroup) as any;

        expect(edition).toMatchObject({});
      });

      it('should return IEdition', () => {
        const formGroup = service.createEditionFormGroup(sampleWithRequiredData);

        const edition = service.getEdition(formGroup) as any;

        expect(edition).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEdition should not enable id FormControl', () => {
        const formGroup = service.createEditionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEdition should disable id FormControl', () => {
        const formGroup = service.createEditionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
