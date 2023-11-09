export interface IStyleDeux {
  id: number;
  styleOfTextDeux?: string | null;
}

export type NewStyleDeux = Omit<IStyleDeux, 'id'> & { id: null };
