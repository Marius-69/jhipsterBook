import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { StyleComponent } from './list/style.component';
import { StyleDetailComponent } from './detail/style-detail.component';
import { StyleUpdateComponent } from './update/style-update.component';
import StyleResolve from './route/style-routing-resolve.service';

const styleRoute: Routes = [
  {
    path: '',
    component: StyleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StyleDetailComponent,
    resolve: {
      style: StyleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StyleUpdateComponent,
    resolve: {
      style: StyleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StyleUpdateComponent,
    resolve: {
      style: StyleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default styleRoute;
