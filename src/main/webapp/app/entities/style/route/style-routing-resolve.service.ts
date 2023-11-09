import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStyle } from '../style.model';
import { StyleService } from '../service/style.service';

export const styleResolve = (route: ActivatedRouteSnapshot): Observable<null | IStyle> => {
  const id = route.params['id'];
  if (id) {
    return inject(StyleService)
      .find(id)
      .pipe(
        mergeMap((style: HttpResponse<IStyle>) => {
          if (style.body) {
            return of(style.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default styleResolve;
