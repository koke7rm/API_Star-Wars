jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BatallaService } from '../service/batalla.service';
import { IBatalla, Batalla } from '../batalla.model';
import { IPersonaje } from 'app/entities/personaje/personaje.model';
import { PersonajeService } from 'app/entities/personaje/service/personaje.service';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { PeliculaService } from 'app/entities/pelicula/service/pelicula.service';
import { IBando } from 'app/entities/bando/bando.model';
import { BandoService } from 'app/entities/bando/service/bando.service';

import { BatallaUpdateComponent } from './batalla-update.component';

describe('Component Tests', () => {
  describe('Batalla Management Update Component', () => {
    let comp: BatallaUpdateComponent;
    let fixture: ComponentFixture<BatallaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let batallaService: BatallaService;
    let personajeService: PersonajeService;
    let peliculaService: PeliculaService;
    let bandoService: BandoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BatallaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BatallaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BatallaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      batallaService = TestBed.inject(BatallaService);
      personajeService = TestBed.inject(PersonajeService);
      peliculaService = TestBed.inject(PeliculaService);
      bandoService = TestBed.inject(BandoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Personaje query and add missing value', () => {
        const batalla: IBatalla = { id: 456 };
        const involucrados: IPersonaje[] = [{ id: 47332 }];
        batalla.involucrados = involucrados;

        const personajeCollection: IPersonaje[] = [{ id: 90362 }];
        spyOn(personajeService, 'query').and.returnValue(of(new HttpResponse({ body: personajeCollection })));
        const additionalPersonajes = [...involucrados];
        const expectedCollection: IPersonaje[] = [...additionalPersonajes, ...personajeCollection];
        spyOn(personajeService, 'addPersonajeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ batalla });
        comp.ngOnInit();

        expect(personajeService.query).toHaveBeenCalled();
        expect(personajeService.addPersonajeToCollectionIfMissing).toHaveBeenCalledWith(personajeCollection, ...additionalPersonajes);
        expect(comp.personajesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Pelicula query and add missing value', () => {
        const batalla: IBatalla = { id: 456 };
        const pelicula: IPelicula = { id: 14651 };
        batalla.pelicula = pelicula;

        const peliculaCollection: IPelicula[] = [{ id: 87235 }];
        spyOn(peliculaService, 'query').and.returnValue(of(new HttpResponse({ body: peliculaCollection })));
        const additionalPeliculas = [pelicula];
        const expectedCollection: IPelicula[] = [...additionalPeliculas, ...peliculaCollection];
        spyOn(peliculaService, 'addPeliculaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ batalla });
        comp.ngOnInit();

        expect(peliculaService.query).toHaveBeenCalled();
        expect(peliculaService.addPeliculaToCollectionIfMissing).toHaveBeenCalledWith(peliculaCollection, ...additionalPeliculas);
        expect(comp.peliculasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Bando query and add missing value', () => {
        const batalla: IBatalla = { id: 456 };
        const ganador: IBando = { id: 75959 };
        batalla.ganador = ganador;
        const perdedor: IBando = { id: 36099 };
        batalla.perdedor = perdedor;

        const bandoCollection: IBando[] = [{ id: 41385 }];
        spyOn(bandoService, 'query').and.returnValue(of(new HttpResponse({ body: bandoCollection })));
        const additionalBandos = [ganador, perdedor];
        const expectedCollection: IBando[] = [...additionalBandos, ...bandoCollection];
        spyOn(bandoService, 'addBandoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ batalla });
        comp.ngOnInit();

        expect(bandoService.query).toHaveBeenCalled();
        expect(bandoService.addBandoToCollectionIfMissing).toHaveBeenCalledWith(bandoCollection, ...additionalBandos);
        expect(comp.bandosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const batalla: IBatalla = { id: 456 };
        const involucrados: IPersonaje = { id: 95281 };
        batalla.involucrados = [involucrados];
        const pelicula: IPelicula = { id: 74565 };
        batalla.pelicula = pelicula;
        const ganador: IBando = { id: 18783 };
        batalla.ganador = ganador;
        const perdedor: IBando = { id: 21318 };
        batalla.perdedor = perdedor;

        activatedRoute.data = of({ batalla });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(batalla));
        expect(comp.personajesSharedCollection).toContain(involucrados);
        expect(comp.peliculasSharedCollection).toContain(pelicula);
        expect(comp.bandosSharedCollection).toContain(ganador);
        expect(comp.bandosSharedCollection).toContain(perdedor);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const batalla = { id: 123 };
        spyOn(batallaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ batalla });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: batalla }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(batallaService.update).toHaveBeenCalledWith(batalla);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const batalla = new Batalla();
        spyOn(batallaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ batalla });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: batalla }));
        saveSubject.complete();

        // THEN
        expect(batallaService.create).toHaveBeenCalledWith(batalla);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const batalla = { id: 123 };
        spyOn(batallaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ batalla });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(batallaService.update).toHaveBeenCalledWith(batalla);
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

      describe('trackPeliculaById', () => {
        it('Should return tracked Pelicula primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPeliculaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackBandoById', () => {
        it('Should return tracked Bando primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBandoById(0, entity);
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
