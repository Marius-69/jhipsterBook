import { IStyle, NewStyle } from './style.model';

export const sampleWithRequiredData: IStyle = {
  id: 30324,
};

export const sampleWithPartialData: IStyle = {
  id: 23991,
};

export const sampleWithFullData: IStyle = {
  id: 12701,
  styleOfText: 'là timide',
};

export const sampleWithNewData: NewStyle = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
