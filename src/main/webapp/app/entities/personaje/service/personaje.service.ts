import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonaje, getPersonajeIdentifier } from '../personaje.model';

export type EntityResponseType = HttpResponse<IPersonaje>;
export type EntityArrayResponseType = HttpResponse<IPersonaje[]>;

@Injectable({ providedIn: 'root' })
export class PersonajeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/personajes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(personaje: IPersonaje): Observable<EntityResponseType> {
    return this.http.post<IPersonaje>(this.resourceUrl, personaje, { observe: 'response' });
  }

  update(personaje: IPersonaje): Observable<EntityResponseType> {
    return this.http.put<IPersonaje>(`${this.resourceUrl}/${getPersonajeIdentifier(personaje) as number}`, personaje, {
      observe: 'response',
    });
  }

  partialUpdate(personaje: IPersonaje): Observable<EntityResponseType> {
    return this.http.patch<IPersonaje>(`${this.resourceUrl}/${getPersonajeIdentifier(personaje) as number}`, personaje, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonaje>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonaje[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonajeToCollectionIfMissing(
    personajeCollection: IPersonaje[],
    ...personajesToCheck: (IPersonaje | null | undefined)[]
  ): IPersonaje[] {
    const personajes: IPersonaje[] = personajesToCheck.filter(isPresent);
    if (personajes.length > 0) {
      const personajeCollectionIdentifiers = personajeCollection.map(personajeItem => getPersonajeIdentifier(personajeItem)!);
      const personajesToAdd = personajes.filter(personajeItem => {
        const personajeIdentifier = getPersonajeIdentifier(personajeItem);
        if (personajeIdentifier == null || personajeCollectionIdentifiers.includes(personajeIdentifier)) {
          return false;
        }
        personajeCollectionIdentifiers.push(personajeIdentifier);
        return true;
      });
      return [...personajesToAdd, ...personajeCollection];
    }
    return personajeCollection;
  }
}
