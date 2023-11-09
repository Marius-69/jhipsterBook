import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStyleDeux } from '../style-deux.model';
import { StyleDeuxService } from '../service/style-deux.service';

export const styleDeuxResolve = (route: ActivatedRouteSnapshot): Observable<null | IStyleDeux> => {
  const id = route.params['id'];
  if (id) {
    return inject(StyleDeuxService)
      .find(id)
      .pipe(
        mergeMap((styleDeux: HttpResponse<IStyleDeux>) => {
          if (styleDeux.body) {
            return of(styleDeux.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default styleDeuxResolve;
