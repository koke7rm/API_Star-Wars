import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBando, Bando } from '../bando.model';

import { BandoService } from './bando.service';

describe('Service Tests', () => {
  describe('Bando Service', () => {
    let service: BandoService;
    let httpMock: HttpTestingController;
    let elemDefault: IBando;
    let expectedResult: IBando | IBando[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BandoService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        logoContentType: 'image/png',
        logo: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Bando', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Bando()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Bando', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            logo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Bando', () => {
        const patchObject = Object.assign({}, new Bando());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Bando', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            logo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Bando', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBandoToCollectionIfMissing', () => {
        it('should add a Bando to an empty array', () => {
          const bando: IBando = { id: 123 };
          expectedResult = service.addBandoToCollectionIfMissing([], bando);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(bando);
        });

        it('should not add a Bando to an array that contains it', () => {
          const bando: IBando = { id: 123 };
          const bandoCollection: IBando[] = [
            {
              ...bando,
            },
            { id: 456 },
          ];
          expectedResult = service.addBandoToCollectionIfMissing(bandoCollection, bando);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Bando to an array that doesn't contain it", () => {
          const bando: IBando = { id: 123 };
          const bandoCollection: IBando[] = [{ id: 456 }];
          expectedResult = service.addBandoToCollectionIfMissing(bandoCollection, bando);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(bando);
        });

        it('should add only unique Bando to an array', () => {
          const bandoArray: IBando[] = [{ id: 123 }, { id: 456 }, { id: 73149 }];
          const bandoCollection: IBando[] = [{ id: 123 }];
          expectedResult = service.addBandoToCollectionIfMissing(bandoCollection, ...bandoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const bando: IBando = { id: 123 };
          const bando2: IBando = { id: 456 };
          expectedResult = service.addBandoToCollectionIfMissing([], bando, bando2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(bando);
          expect(expectedResult).toContain(bando2);
        });

        it('should accept null and undefined values', () => {
          const bando: IBando = { id: 123 };
          expectedResult = service.addBandoToCollectionIfMissing([], null, bando, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(bando);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
