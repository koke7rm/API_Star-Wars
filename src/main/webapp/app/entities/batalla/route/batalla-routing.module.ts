import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BatallaComponent } from '../list/batalla.component';
import { BatallaDetailComponent } from '../detail/batalla-detail.component';
import { BatallaUpdateComponent } from '../update/batalla-update.component';
import { BatallaRoutingResolveService } from './batalla-routing-resolve.service';

const batallaRoute: Routes = [
  {
    path: '',
    component: BatallaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BatallaDetailComponent,
    resolve: {
      batalla: BatallaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BatallaUpdateComponent,
    resolve: {
      batalla: BatallaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BatallaUpdateComponent,
    resolve: {
      batalla: BatallaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(batallaRoute)],
  exports: [RouterModule],
})
export class BatallaRoutingModule {}
