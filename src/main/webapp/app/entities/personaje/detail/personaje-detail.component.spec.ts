import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonajeDetailComponent } from './personaje-detail.component';

describe('Component Tests', () => {
  describe('Personaje Management Detail Component', () => {
    let comp: PersonajeDetailComponent;
    let fixture: ComponentFixture<PersonajeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PersonajeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ personaje: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PersonajeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PersonajeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load personaje on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.personaje).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
