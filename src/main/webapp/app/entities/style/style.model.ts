import { IBook } from 'app/entities/book/book.model';

export interface IStyle {
  id: number;
  styleOfText?: string | null;
  book?: Pick<IBook, 'id'> | null;
}

export type NewStyle = Omit<IStyle, 'id'> & { id: null };
