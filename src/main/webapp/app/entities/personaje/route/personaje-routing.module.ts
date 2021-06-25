import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonajeComponent } from '../list/personaje.component';
import { PersonajeDetailComponent } from '../detail/personaje-detail.component';
import { PersonajeUpdateComponent } from '../update/personaje-update.component';
import { PersonajeRoutingResolveService } from './personaje-routing-resolve.service';

const personajeRoute: Routes = [
  {
    path: '',
    component: PersonajeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonajeDetailComponent,
    resolve: {
      personaje: PersonajeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonajeUpdateComponent,
    resolve: {
      personaje: PersonajeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonajeUpdateComponent,
    resolve: {
      personaje: PersonajeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personajeRoute)],
  exports: [RouterModule],
})
export class PersonajeRoutingModule {}
