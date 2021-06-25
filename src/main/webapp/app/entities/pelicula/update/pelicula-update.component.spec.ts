jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PeliculaService } from '../service/pelicula.service';
import { IPelicula, Pelicula } from '../pelicula.model';
import { IPersonaje } from 'app/entities/personaje/personaje.model';
import { PersonajeService } from 'app/entities/personaje/service/personaje.service';

import { PeliculaUpdateComponent } from './pelicula-update.component';

describe('Component Tests', () => {
  describe('Pelicula Management Update Component', () => {
    let comp: PeliculaUpdateComponent;
    let fixture: ComponentFixture<PeliculaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let peliculaService: PeliculaService;
    let personajeService: PersonajeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PeliculaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PeliculaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeliculaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      peliculaService = TestBed.inject(PeliculaService);
      personajeService = TestBed.inject(PersonajeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Personaje query and add missing value', () => {
        const pelicula: IPelicula = { id: 456 };
        const personajes: IPersonaje[] = [{ id: 99273 }];
        pelicula.personajes = personajes;

        const personajeCollection: IPersonaje[] = [{ id: 54075 }];
        spyOn(personajeService, 'query').and.returnValue(of(new HttpResponse({ body: personajeCollection })));
        const additionalPersonajes = [...personajes];
        const expectedCollection: IPersonaje[] = [...additionalPersonajes, ...personajeCollection];
        spyOn(personajeService, 'addPersonajeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ pelicula });
        comp.ngOnInit();

        expect(personajeService.query).toHaveBeenCalled();
        expect(personajeService.addPersonajeToCollectionIfMissing).toHaveBeenCalledWith(personajeCollection, ...additionalPersonajes);
        expect(comp.personajesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pelicula: IPelicula = { id: 456 };
        const personajes: IPersonaje = { id: 5637 };
        pelicula.personajes = [personajes];

        activatedRoute.data = of({ pelicula });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pelicula));
        expect(comp.personajesSharedCollection).toContain(personajes);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pelicula = { id: 123 };
        spyOn(peliculaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pelicula });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pelicula }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(peliculaService.update).toHaveBeenCalledWith(pelicula);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pelicula = new Pelicula();
        spyOn(peliculaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pelicula });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pelicula }));
        saveSubject.complete();

        // THEN
        expect(peliculaService.create).toHaveBeenCalledWith(pelicula);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pelicula = { id: 123 };
        spyOn(peliculaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pelicula });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(peliculaService.update).toHaveBeenCalledWith(pelicula);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPersonajeById', () => {
        it('Should return tracked Personaje primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonajeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedPersonaje', () => {
        it('Should return option if no Personaje is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedPersonaje(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Personaje for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedPersonaje(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Personaje is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedPersonaje(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
