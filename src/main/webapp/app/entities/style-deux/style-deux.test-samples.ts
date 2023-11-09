import { IStyleDeux, NewStyleDeux } from './style-deux.model';

export const sampleWithRequiredData: IStyleDeux = {
  id: 27883,
};

export const sampleWithPartialData: IStyleDeux = {
  id: 19135,
  styleOfTextDeux: 'assez depuis dissocier',
};

export const sampleWithFullData: IStyleDeux = {
  id: 2275,
  styleOfTextDeux: 'à seule fin de quelquefois',
};

export const sampleWithNewData: NewStyleDeux = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
