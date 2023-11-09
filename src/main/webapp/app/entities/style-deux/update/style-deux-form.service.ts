import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStyleDeux, NewStyleDeux } from '../style-deux.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStyleDeux for edit and NewStyleDeuxFormGroupInput for create.
 */
type StyleDeuxFormGroupInput = IStyleDeux | PartialWithRequiredKeyOf<NewStyleDeux>;

type StyleDeuxFormDefaults = Pick<NewStyleDeux, 'id'>;

type StyleDeuxFormGroupContent = {
  id: FormControl<IStyleDeux['id'] | NewStyleDeux['id']>;
  styleOfTextDeux: FormControl<IStyleDeux['styleOfTextDeux']>;
};

export type StyleDeuxFormGroup = FormGroup<StyleDeuxFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StyleDeuxFormService {
  createStyleDeuxFormGroup(styleDeux: StyleDeuxFormGroupInput = { id: null }): StyleDeuxFormGroup {
    const styleDeuxRawValue = {
      ...this.getFormDefaults(),
      ...styleDeux,
    };
    return new FormGroup<StyleDeuxFormGroupContent>({
      id: new FormControl(
        { value: styleDeuxRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      styleOfTextDeux: new FormControl(styleDeuxRawValue.styleOfTextDeux),
    });
  }

  getStyleDeux(form: StyleDeuxFormGroup): IStyleDeux | NewStyleDeux {
    return form.getRawValue() as IStyleDeux | NewStyleDeux;
  }

  resetForm(form: StyleDeuxFormGroup, styleDeux: StyleDeuxFormGroupInput): void {
    const styleDeuxRawValue = { ...this.getFormDefaults(), ...styleDeux };
    form.reset(
      {
        ...styleDeuxRawValue,
        id: { value: styleDeuxRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StyleDeuxFormDefaults {
    return {
      id: null,
    };
  }
}
