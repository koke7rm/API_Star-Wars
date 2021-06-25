jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonajeService } from '../service/personaje.service';
import { IPersonaje, Personaje } from '../personaje.model';

import { PersonajeUpdateComponent } from './personaje-update.component';

describe('Component Tests', () => {
  describe('Personaje Management Update Component', () => {
    let comp: PersonajeUpdateComponent;
    let fixture: ComponentFixture<PersonajeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personajeService: PersonajeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonajeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonajeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonajeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personajeService = TestBed.inject(PersonajeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const personaje: IPersonaje = { id: 456 };

        activatedRoute.data = of({ personaje });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(personaje));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personaje = { id: 123 };
        spyOn(personajeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personaje });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personaje }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personajeService.update).toHaveBeenCalledWith(personaje);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personaje = new Personaje();
        spyOn(personajeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personaje });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personaje }));
        saveSubject.complete();

        // THEN
        expect(personajeService.create).toHaveBeenCalledWith(personaje);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personaje = { id: 123 };
        spyOn(personajeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personaje });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personajeService.update).toHaveBeenCalledWith(personaje);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
