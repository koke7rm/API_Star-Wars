jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBando, Bando } from '../bando.model';
import { BandoService } from '../service/bando.service';

import { BandoRoutingResolveService } from './bando-routing-resolve.service';

describe('Service Tests', () => {
  describe('Bando routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BandoRoutingResolveService;
    let service: BandoService;
    let resultBando: IBando | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BandoRoutingResolveService);
      service = TestBed.inject(BandoService);
      resultBando = undefined;
    });

    describe('resolve', () => {
      it('should return IBando returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBando = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBando).toEqual({ id: 123 });
      });

      it('should return new IBando if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBando = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBando).toEqual(new Bando());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBando = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBando).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
