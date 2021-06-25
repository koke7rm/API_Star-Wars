jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPersonaje, Personaje } from '../personaje.model';
import { PersonajeService } from '../service/personaje.service';

import { PersonajeRoutingResolveService } from './personaje-routing-resolve.service';

describe('Service Tests', () => {
  describe('Personaje routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PersonajeRoutingResolveService;
    let service: PersonajeService;
    let resultPersonaje: IPersonaje | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PersonajeRoutingResolveService);
      service = TestBed.inject(PersonajeService);
      resultPersonaje = undefined;
    });

    describe('resolve', () => {
      it('should return IPersonaje returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonaje = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonaje).toEqual({ id: 123 });
      });

      it('should return new IPersonaje if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonaje = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPersonaje).toEqual(new Personaje());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonaje = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonaje).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
