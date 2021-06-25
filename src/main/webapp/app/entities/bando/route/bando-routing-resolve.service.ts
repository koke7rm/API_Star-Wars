import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBando, Bando } from '../bando.model';
import { BandoService } from '../service/bando.service';

@Injectable({ providedIn: 'root' })
export class BandoRoutingResolveService implements Resolve<IBando> {
  constructor(protected service: BandoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBando> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bando: HttpResponse<Bando>) => {
          if (bando.body) {
            return of(bando.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bando());
  }
}
