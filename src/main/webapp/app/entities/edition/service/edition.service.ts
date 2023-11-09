import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEdition, NewEdition } from '../edition.model';

export type PartialUpdateEdition = Partial<IEdition> & Pick<IEdition, 'id'>;

export type EntityResponseType = HttpResponse<IEdition>;
export type EntityArrayResponseType = HttpResponse<IEdition[]>;

@Injectable({ providedIn: 'root' })
export class EditionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/editions');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(edition: NewEdition): Observable<EntityResponseType> {
    return this.http.post<IEdition>(this.resourceUrl, edition, { observe: 'response' });
  }

  update(edition: IEdition): Observable<EntityResponseType> {
    return this.http.put<IEdition>(`${this.resourceUrl}/${this.getEditionIdentifier(edition)}`, edition, { observe: 'response' });
  }

  partialUpdate(edition: PartialUpdateEdition): Observable<EntityResponseType> {
    return this.http.patch<IEdition>(`${this.resourceUrl}/${this.getEditionIdentifier(edition)}`, edition, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEdition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEdition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEditionIdentifier(edition: Pick<IEdition, 'id'>): number {
    return edition.id;
  }

  compareEdition(o1: Pick<IEdition, 'id'> | null, o2: Pick<IEdition, 'id'> | null): boolean {
    return o1 && o2 ? this.getEditionIdentifier(o1) === this.getEditionIdentifier(o2) : o1 === o2;
  }

  addEditionToCollectionIfMissing<Type extends Pick<IEdition, 'id'>>(
    editionCollection: Type[],
    ...editionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const editions: Type[] = editionsToCheck.filter(isPresent);
    if (editions.length > 0) {
      const editionCollectionIdentifiers = editionCollection.map(editionItem => this.getEditionIdentifier(editionItem)!);
      const editionsToAdd = editions.filter(editionItem => {
        const editionIdentifier = this.getEditionIdentifier(editionItem);
        if (editionCollectionIdentifiers.includes(editionIdentifier)) {
          return false;
        }
        editionCollectionIdentifiers.push(editionIdentifier);
        return true;
      });
      return [...editionsToAdd, ...editionCollection];
    }
    return editionCollection;
  }
}
