import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersonaje, Personaje } from '../personaje.model';

import { PersonajeService } from './personaje.service';

describe('Service Tests', () => {
  describe('Personaje Service', () => {
    let service: PersonajeService;
    let httpMock: HttpTestingController;
    let elemDefault: IPersonaje;
    let expectedResult: IPersonaje | IPersonaje[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PersonajeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        edad: 0,
        rango: 'AAAAAAA',
        especie: 'AAAAAAA',
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

      it('should create a Personaje', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Personaje()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Personaje', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            edad: 1,
            rango: 'BBBBBB',
            especie: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Personaje', () => {
        const patchObject = Object.assign(
          {
            edad: 1,
            rango: 'BBBBBB',
          },
          new Personaje()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Personaje', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            edad: 1,
            rango: 'BBBBBB',
            especie: 'BBBBBB',
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

      it('should delete a Personaje', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPersonajeToCollectionIfMissing', () => {
        it('should add a Personaje to an empty array', () => {
          const personaje: IPersonaje = { id: 123 };
          expectedResult = service.addPersonajeToCollectionIfMissing([], personaje);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personaje);
        });

        it('should not add a Personaje to an array that contains it', () => {
          const personaje: IPersonaje = { id: 123 };
          const personajeCollection: IPersonaje[] = [
            {
              ...personaje,
            },
            { id: 456 },
          ];
          expectedResult = service.addPersonajeToCollectionIfMissing(personajeCollection, personaje);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Personaje to an array that doesn't contain it", () => {
          const personaje: IPersonaje = { id: 123 };
          const personajeCollection: IPersonaje[] = [{ id: 456 }];
          expectedResult = service.addPersonajeToCollectionIfMissing(personajeCollection, personaje);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personaje);
        });

        it('should add only unique Personaje to an array', () => {
          const personajeArray: IPersonaje[] = [{ id: 123 }, { id: 456 }, { id: 31374 }];
          const personajeCollection: IPersonaje[] = [{ id: 123 }];
          expectedResult = service.addPersonajeToCollectionIfMissing(personajeCollection, ...personajeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const personaje: IPersonaje = { id: 123 };
          const personaje2: IPersonaje = { id: 456 };
          expectedResult = service.addPersonajeToCollectionIfMissing([], personaje, personaje2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personaje);
          expect(expectedResult).toContain(personaje2);
        });

        it('should accept null and undefined values', () => {
          const personaje: IPersonaje = { id: 123 };
          expectedResult = service.addPersonajeToCollectionIfMissing([], null, personaje, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personaje);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
