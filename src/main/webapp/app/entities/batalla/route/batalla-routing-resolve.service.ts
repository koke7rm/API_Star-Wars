import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBatalla, Batalla } from '../batalla.model';
import { BatallaService } from '../service/batalla.service';

@Injectable({ providedIn: 'root' })
export class BatallaRoutingResolveService implements Resolve<IBatalla> {
  constructor(protected service: BatallaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBatalla> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((batalla: HttpResponse<Batalla>) => {
          if (batalla.body) {
            return of(batalla.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Batalla());
  }
}
