import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PersonajeService } from '../service/personaje.service';

import { PersonajeComponent } from './personaje.component';

describe('Component Tests', () => {
  describe('Personaje Management Component', () => {
    let comp: PersonajeComponent;
    let fixture: ComponentFixture<PersonajeComponent>;
    let service: PersonajeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonajeComponent],
      })
        .overrideTemplate(PersonajeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonajeComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PersonajeService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.personajes?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
