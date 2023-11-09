import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStyle, NewStyle } from '../style.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStyle for edit and NewStyleFormGroupInput for create.
 */
type StyleFormGroupInput = IStyle | PartialWithRequiredKeyOf<NewStyle>;

type StyleFormDefaults = Pick<NewStyle, 'id'>;

type StyleFormGroupContent = {
  id: FormControl<IStyle['id'] | NewStyle['id']>;
  styleOfText: FormControl<IStyle['styleOfText']>;
  book: FormControl<IStyle['book']>;
};

export type StyleFormGroup = FormGroup<StyleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StyleFormService {
  createStyleFormGroup(style: StyleFormGroupInput = { id: null }): StyleFormGroup {
    const styleRawValue = {
      ...this.getFormDefaults(),
      ...style,
    };
    return new FormGroup<StyleFormGroupContent>({
      id: new FormControl(
        { value: styleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      styleOfText: new FormControl(styleRawValue.styleOfText),
      book: new FormControl(styleRawValue.book),
    });
  }

  getStyle(form: StyleFormGroup): IStyle | NewStyle {
    return form.getRawValue() as IStyle | NewStyle;
  }

  resetForm(form: StyleFormGroup, style: StyleFormGroupInput): void {
    const styleRawValue = { ...this.getFormDefaults(), ...style };
    form.reset(
      {
        ...styleRawValue,
        id: { value: styleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StyleFormDefaults {
    return {
      id: null,
    };
  }
}
