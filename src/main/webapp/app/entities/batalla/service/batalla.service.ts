import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBatalla, getBatallaIdentifier } from '../batalla.model';

export type EntityResponseType = HttpResponse<IBatalla>;
export type EntityArrayResponseType = HttpResponse<IBatalla[]>;

@Injectable({ providedIn: 'root' })
export class BatallaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/batallas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(batalla: IBatalla): Observable<EntityResponseType> {
    return this.http.post<IBatalla>(this.resourceUrl, batalla, { observe: 'response' });
  }

  update(batalla: IBatalla): Observable<EntityResponseType> {
    return this.http.put<IBatalla>(`${this.resourceUrl}/${getBatallaIdentifier(batalla) as number}`, batalla, { observe: 'response' });
  }

  partialUpdate(batalla: IBatalla): Observable<EntityResponseType> {
    return this.http.patch<IBatalla>(`${this.resourceUrl}/${getBatallaIdentifier(batalla) as number}`, batalla, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBatalla>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBatalla[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBatallaToCollectionIfMissing(batallaCollection: IBatalla[], ...batallasToCheck: (IBatalla | null | undefined)[]): IBatalla[] {
    const batallas: IBatalla[] = batallasToCheck.filter(isPresent);
    if (batallas.length > 0) {
      const batallaCollectionIdentifiers = batallaCollection.map(batallaItem => getBatallaIdentifier(batallaItem)!);
      const batallasToAdd = batallas.filter(batallaItem => {
        const batallaIdentifier = getBatallaIdentifier(batallaItem);
        if (batallaIdentifier == null || batallaCollectionIdentifiers.includes(batallaIdentifier)) {
          return false;
        }
        batallaCollectionIdentifiers.push(batallaIdentifier);
        return true;
      });
      return [...batallasToAdd, ...batallaCollection];
    }
    return batallaCollection;
  }
}
