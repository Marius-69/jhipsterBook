import { IAuthor, NewAuthor } from './author.model';

export const sampleWithRequiredData: IAuthor = {
  id: 21635,
};

export const sampleWithPartialData: IAuthor = {
  id: 21904,
};

export const sampleWithFullData: IAuthor = {
  id: 4438,
  nameAuthor: 'foule ha ha de manière à ce que',
  surnameAuthor: 'mieux',
};

export const sampleWithNewData: NewAuthor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
