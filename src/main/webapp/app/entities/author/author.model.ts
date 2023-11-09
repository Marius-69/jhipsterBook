export interface IAuthor {
  id: number;
  nameAuthor?: string | null;
  surnameAuthor?: string | null;
}

export type NewAuthor = Omit<IAuthor, 'id'> & { id: null };
