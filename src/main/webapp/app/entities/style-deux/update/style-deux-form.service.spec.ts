import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../style-deux.test-samples';

import { StyleDeuxFormService } from './style-deux-form.service';

describe('StyleDeux Form Service', () => {
  let service: StyleDeuxFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StyleDeuxFormService);
  });

  describe('Service methods', () => {
    describe('createStyleDeuxFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStyleDeuxFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            styleOfTextDeux: expect.any(Object),
          }),
        );
      });

      it('passing IStyleDeux should create a new form with FormGroup', () => {
        const formGroup = service.createStyleDeuxFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            styleOfTextDeux: expect.any(Object),
          }),
        );
      });
    });

    describe('getStyleDeux', () => {
      it('should return NewStyleDeux for default StyleDeux initial value', () => {
        const formGroup = service.createStyleDeuxFormGroup(sampleWithNewData);

        const styleDeux = service.getStyleDeux(formGroup) as any;

        expect(styleDeux).toMatchObject(sampleWithNewData);
      });

      it('should return NewStyleDeux for empty StyleDeux initial value', () => {
        const formGroup = service.createStyleDeuxFormGroup();

        const styleDeux = service.getStyleDeux(formGroup) as any;

        expect(styleDeux).toMatchObject({});
      });

      it('should return IStyleDeux', () => {
        const formGroup = service.createStyleDeuxFormGroup(sampleWithRequiredData);

        const styleDeux = service.getStyleDeux(formGroup) as any;

        expect(styleDeux).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStyleDeux should not enable id FormControl', () => {
        const formGroup = service.createStyleDeuxFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStyleDeux should disable id FormControl', () => {
        const formGroup = service.createStyleDeuxFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
