import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStyleDeux, NewStyleDeux } from '../style-deux.model';

export type PartialUpdateStyleDeux = Partial<IStyleDeux> & Pick<IStyleDeux, 'id'>;

export type EntityResponseType = HttpResponse<IStyleDeux>;
export type EntityArrayResponseType = HttpResponse<IStyleDeux[]>;

@Injectable({ providedIn: 'root' })
export class StyleDeuxService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/style-deuxes');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(styleDeux: NewStyleDeux): Observable<EntityResponseType> {
    return this.http.post<IStyleDeux>(this.resourceUrl, styleDeux, { observe: 'response' });
  }

  update(styleDeux: IStyleDeux): Observable<EntityResponseType> {
    return this.http.put<IStyleDeux>(`${this.resourceUrl}/${this.getStyleDeuxIdentifier(styleDeux)}`, styleDeux, { observe: 'response' });
  }

  partialUpdate(styleDeux: PartialUpdateStyleDeux): Observable<EntityResponseType> {
    return this.http.patch<IStyleDeux>(`${this.resourceUrl}/${this.getStyleDeuxIdentifier(styleDeux)}`, styleDeux, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStyleDeux>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStyleDeux[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStyleDeuxIdentifier(styleDeux: Pick<IStyleDeux, 'id'>): number {
    return styleDeux.id;
  }

  compareStyleDeux(o1: Pick<IStyleDeux, 'id'> | null, o2: Pick<IStyleDeux, 'id'> | null): boolean {
    return o1 && o2 ? this.getStyleDeuxIdentifier(o1) === this.getStyleDeuxIdentifier(o2) : o1 === o2;
  }

  addStyleDeuxToCollectionIfMissing<Type extends Pick<IStyleDeux, 'id'>>(
    styleDeuxCollection: Type[],
    ...styleDeuxesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const styleDeuxes: Type[] = styleDeuxesToCheck.filter(isPresent);
    if (styleDeuxes.length > 0) {
      const styleDeuxCollectionIdentifiers = styleDeuxCollection.map(styleDeuxItem => this.getStyleDeuxIdentifier(styleDeuxItem)!);
      const styleDeuxesToAdd = styleDeuxes.filter(styleDeuxItem => {
        const styleDeuxIdentifier = this.getStyleDeuxIdentifier(styleDeuxItem);
        if (styleDeuxCollectionIdentifiers.includes(styleDeuxIdentifier)) {
          return false;
        }
        styleDeuxCollectionIdentifiers.push(styleDeuxIdentifier);
        return true;
      });
      return [...styleDeuxesToAdd, ...styleDeuxCollection];
    }
    return styleDeuxCollection;
  }
}
