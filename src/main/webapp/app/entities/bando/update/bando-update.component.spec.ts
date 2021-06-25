jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BandoService } from '../service/bando.service';
import { IBando, Bando } from '../bando.model';
import { IPersonaje } from 'app/entities/personaje/personaje.model';
import { PersonajeService } from 'app/entities/personaje/service/personaje.service';

import { BandoUpdateComponent } from './bando-update.component';

describe('Component Tests', () => {
  describe('Bando Management Update Component', () => {
    let comp: BandoUpdateComponent;
    let fixture: ComponentFixture<BandoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let bandoService: BandoService;
    let personajeService: PersonajeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BandoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BandoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BandoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      bandoService = TestBed.inject(BandoService);
      personajeService = TestBed.inject(PersonajeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Personaje query and add missing value', () => {
        const bando: IBando = { id: 456 };
        const integrantes: IPersonaje = { id: 6677 };
        bando.integrantes = integrantes;

        const personajeCollection: IPersonaje[] = [{ id: 97192 }];
        spyOn(personajeService, 'query').and.returnValue(of(new HttpResponse({ body: personajeCollection })));
        const additionalPersonajes = [integrantes];
        const expectedCollection: IPersonaje[] = [...additionalPersonajes, ...personajeCollection];
        spyOn(personajeService, 'addPersonajeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ bando });
        comp.ngOnInit();

        expect(personajeService.query).toHaveBeenCalled();
        expect(personajeService.addPersonajeToCollectionIfMissing).toHaveBeenCalledWith(personajeCollection, ...additionalPersonajes);
        expect(comp.personajesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const bando: IBando = { id: 456 };
        const integrantes: IPersonaje = { id: 55651 };
        bando.integrantes = integrantes;

        activatedRoute.data = of({ bando });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(bando));
        expect(comp.personajesSharedCollection).toContain(integrantes);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bando = { id: 123 };
        spyOn(bandoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bando });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bando }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(bandoService.update).toHaveBeenCalledWith(bando);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bando = new Bando();
        spyOn(bandoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bando });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bando }));
        saveSubject.complete();

        // THEN
        expect(bandoService.create).toHaveBeenCalledWith(bando);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bando = { id: 123 };
        spyOn(bandoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bando });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(bandoService.update).toHaveBeenCalledWith(bando);
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
  });
});
