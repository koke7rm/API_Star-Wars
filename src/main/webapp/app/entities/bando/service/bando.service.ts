import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBando, getBandoIdentifier } from '../bando.model';

export type EntityResponseType = HttpResponse<IBando>;
export type EntityArrayResponseType = HttpResponse<IBando[]>;

@Injectable({ providedIn: 'root' })
export class BandoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/bandos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(bando: IBando): Observable<EntityResponseType> {
    return this.http.post<IBando>(this.resourceUrl, bando, { observe: 'response' });
  }

  update(bando: IBando): Observable<EntityResponseType> {
    return this.http.put<IBando>(`${this.resourceUrl}/${getBandoIdentifier(bando) as number}`, bando, { observe: 'response' });
  }

  partialUpdate(bando: IBando): Observable<EntityResponseType> {
    return this.http.patch<IBando>(`${this.resourceUrl}/${getBandoIdentifier(bando) as number}`, bando, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBando>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBando[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBandoToCollectionIfMissing(bandoCollection: IBando[], ...bandosToCheck: (IBando | null | undefined)[]): IBando[] {
    const bandos: IBando[] = bandosToCheck.filter(isPresent);
    if (bandos.length > 0) {
      const bandoCollectionIdentifiers = bandoCollection.map(bandoItem => getBandoIdentifier(bandoItem)!);
      const bandosToAdd = bandos.filter(bandoItem => {
        const bandoIdentifier = getBandoIdentifier(bandoItem);
        if (bandoIdentifier == null || bandoCollectionIdentifiers.includes(bandoIdentifier)) {
          return false;
        }
        bandoCollectionIdentifiers.push(bandoIdentifier);
        return true;
      });
      return [...bandosToAdd, ...bandoCollection];
    }
    return bandoCollection;
  }
}
