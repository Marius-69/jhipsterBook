import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EditionComponent } from './list/edition.component';
import { EditionDetailComponent } from './detail/edition-detail.component';
import { EditionUpdateComponent } from './update/edition-update.component';
import EditionResolve from './route/edition-routing-resolve.service';

const editionRoute: Routes = [
  {
    path: '',
    component: EditionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EditionDetailComponent,
    resolve: {
      edition: EditionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EditionUpdateComponent,
    resolve: {
      edition: EditionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EditionUpdateComponent,
    resolve: {
      edition: EditionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default editionRoute;
