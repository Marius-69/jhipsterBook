import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEdition, NewEdition } from '../edition.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEdition for edit and NewEditionFormGroupInput for create.
 */
type EditionFormGroupInput = IEdition | PartialWithRequiredKeyOf<NewEdition>;

type EditionFormDefaults = Pick<NewEdition, 'id'>;

type EditionFormGroupContent = {
  id: FormControl<IEdition['id'] | NewEdition['id']>;
  dayOfPublication: FormControl<IEdition['dayOfPublication']>;
  monthOfPublication: FormControl<IEdition['monthOfPublication']>;
};

export type EditionFormGroup = FormGroup<EditionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EditionFormService {
  createEditionFormGroup(edition: EditionFormGroupInput = { id: null }): EditionFormGroup {
    const editionRawValue = {
      ...this.getFormDefaults(),
      ...edition,
    };
    return new FormGroup<EditionFormGroupContent>({
      id: new FormControl(
        { value: editionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dayOfPublication: new FormControl(editionRawValue.dayOfPublication),
      monthOfPublication: new FormControl(editionRawValue.monthOfPublication),
    });
  }

  getEdition(form: EditionFormGroup): IEdition | NewEdition {
    return form.getRawValue() as IEdition | NewEdition;
  }

  resetForm(form: EditionFormGroup, edition: EditionFormGroupInput): void {
    const editionRawValue = { ...this.getFormDefaults(), ...edition };
    form.reset(
      {
        ...editionRawValue,
        id: { value: editionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EditionFormDefaults {
    return {
      id: null,
    };
  }
}
