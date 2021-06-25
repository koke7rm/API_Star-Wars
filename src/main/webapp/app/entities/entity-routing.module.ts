import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'personaje',
        data: { pageTitle: 'Personajes' },
        loadChildren: () => import('./personaje/personaje.module').then(m => m.PersonajeModule),
      },
      {
        path: 'pelicula',
        data: { pageTitle: 'Peliculas' },
        loadChildren: () => import('./pelicula/pelicula.module').then(m => m.PeliculaModule),
      },
      {
        path: 'bando',
        data: { pageTitle: 'Bandos' },
        loadChildren: () => import('./bando/bando.module').then(m => m.BandoModule),
      },
      {
        path: 'batalla',
        data: { pageTitle: 'Batallas' },
        loadChildren: () => import('./batalla/batalla.module').then(m => m.BatallaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
