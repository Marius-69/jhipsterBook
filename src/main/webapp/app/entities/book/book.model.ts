import { IAuthor } from 'app/entities/author/author.model';
import { IEdition } from 'app/entities/edition/edition.model';

export interface IBook {
  id: number;
  title?: string | null;
  author?: Pick<IAuthor, 'id'> | null;
  edition?: Pick<IEdition, 'id'> | null;
}

export type NewBook = Omit<IBook, 'id'> & { id: null };
