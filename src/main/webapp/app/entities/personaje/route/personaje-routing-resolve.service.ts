import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonaje, Personaje } from '../personaje.model';
import { PersonajeService } from '../service/personaje.service';

@Injectable({ providedIn: 'root' })
export class PersonajeRoutingResolveService implements Resolve<IPersonaje> {
  constructor(protected service: PersonajeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonaje> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personaje: HttpResponse<Personaje>) => {
          if (personaje.body) {
            return of(personaje.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Personaje());
  }
}
