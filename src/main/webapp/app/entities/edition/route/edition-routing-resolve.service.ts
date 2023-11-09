import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEdition } from '../edition.model';
import { EditionService } from '../service/edition.service';

export const editionResolve = (route: ActivatedRouteSnapshot): Observable<null | IEdition> => {
  const id = route.params['id'];
  if (id) {
    return inject(EditionService)
      .find(id)
      .pipe(
        mergeMap((edition: HttpResponse<IEdition>) => {
          if (edition.body) {
            return of(edition.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default editionResolve;
