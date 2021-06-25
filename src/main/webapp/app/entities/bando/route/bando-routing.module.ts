import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BandoComponent } from '../list/bando.component';
import { BandoDetailComponent } from '../detail/bando-detail.component';
import { BandoUpdateComponent } from '../update/bando-update.component';
import { BandoRoutingResolveService } from './bando-routing-resolve.service';

const bandoRoute: Routes = [
  {
    path: '',
    component: BandoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BandoDetailComponent,
    resolve: {
      bando: BandoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BandoUpdateComponent,
    resolve: {
      bando: BandoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BandoUpdateComponent,
    resolve: {
      bando: BandoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bandoRoute)],
  exports: [RouterModule],
})
export class BandoRoutingModule {}
