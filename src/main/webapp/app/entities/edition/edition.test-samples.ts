import { IEdition, NewEdition } from './edition.model';

export const sampleWithRequiredData: IEdition = {
  id: 7347,
};

export const sampleWithPartialData: IEdition = {
  id: 23185,
  monthOfPublication: 'pschitt',
};

export const sampleWithFullData: IEdition = {
  id: 31814,
  dayOfPublication: 21024,
  monthOfPublication: 'boum susciter',
};

export const sampleWithNewData: NewEdition = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
