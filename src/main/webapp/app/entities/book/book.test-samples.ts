import { IBook, NewBook } from './book.model';

export const sampleWithRequiredData: IBook = {
  id: 15925,
};

export const sampleWithPartialData: IBook = {
  id: 31003,
  title: 'alors',
};

export const sampleWithFullData: IBook = {
  id: 4851,
  title: 'placide à défaut de  au point que',
};

export const sampleWithNewData: NewBook = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
