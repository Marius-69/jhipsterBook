export interface IEdition {
  id: number;
  dayOfPublication?: number | null;
  monthOfPublication?: string | null;
}

export type NewEdition = Omit<IEdition, 'id'> & { id: null };
