import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBatalla, Batalla } from '../batalla.model';

import { BatallaService } from './batalla.service';

describe('Service Tests', () => {
  describe('Batalla Service', () => {
    let service: BatallaService;
    let httpMock: HttpTestingController;
    let elemDefault: IBatalla;
    let expectedResult: IBatalla | IBatalla[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BatallaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        planeta: 'AAAAAAA',
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

      it('should create a Batalla', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Batalla()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Batalla', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            planeta: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Batalla', () => {
        const patchObject = Object.assign(
          {
            planeta: 'BBBBBB',
          },
          new Batalla()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Batalla', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            planeta: 'BBBBBB',
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

      it('should delete a Batalla', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBatallaToCollectionIfMissing', () => {
        it('should add a Batalla to an empty array', () => {
          const batalla: IBatalla = { id: 123 };
          expectedResult = service.addBatallaToCollectionIfMissing([], batalla);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(batalla);
        });

        it('should not add a Batalla to an array that contains it', () => {
          const batalla: IBatalla = { id: 123 };
          const batallaCollection: IBatalla[] = [
            {
              ...batalla,
            },
            { id: 456 },
          ];
          expectedResult = service.addBatallaToCollectionIfMissing(batallaCollection, batalla);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Batalla to an array that doesn't contain it", () => {
          const batalla: IBatalla = { id: 123 };
          const batallaCollection: IBatalla[] = [{ id: 456 }];
          expectedResult = service.addBatallaToCollectionIfMissing(batallaCollection, batalla);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(batalla);
        });

        it('should add only unique Batalla to an array', () => {
          const batallaArray: IBatalla[] = [{ id: 123 }, { id: 456 }, { id: 88444 }];
          const batallaCollection: IBatalla[] = [{ id: 123 }];
          expectedResult = service.addBatallaToCollectionIfMissing(batallaCollection, ...batallaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const batalla: IBatalla = { id: 123 };
          const batalla2: IBatalla = { id: 456 };
          expectedResult = service.addBatallaToCollectionIfMissing([], batalla, batalla2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(batalla);
          expect(expectedResult).toContain(batalla2);
        });

        it('should accept null and undefined values', () => {
          const batalla: IBatalla = { id: 123 };
          expectedResult = service.addBatallaToCollectionIfMissing([], null, batalla, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(batalla);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
