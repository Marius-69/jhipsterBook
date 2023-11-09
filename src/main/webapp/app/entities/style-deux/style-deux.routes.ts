import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { StyleDeuxComponent } from './list/style-deux.component';
import { StyleDeuxDetailComponent } from './detail/style-deux-detail.component';
import { StyleDeuxUpdateComponent } from './update/style-deux-update.component';
import StyleDeuxResolve from './route/style-deux-routing-resolve.service';

const styleDeuxRoute: Routes = [
  {
    path: '',
    component: StyleDeuxComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StyleDeuxDetailComponent,
    resolve: {
      styleDeux: StyleDeuxResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StyleDeuxUpdateComponent,
    resolve: {
      styleDeux: StyleDeuxResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StyleDeuxUpdateComponent,
    resolve: {
      styleDeux: StyleDeuxResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default styleDeuxRoute;
