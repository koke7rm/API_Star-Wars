import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PeliculaService } from '../service/pelicula.service';

import { PeliculaComponent } from './pelicula.component';

describe('Component Tests', () => {
  describe('Pelicula Management Component', () => {
    let comp: PeliculaComponent;
    let fixture: ComponentFixture<PeliculaComponent>;
    let service: PeliculaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PeliculaComponent],
      })
        .overrideTemplate(PeliculaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeliculaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PeliculaService);

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
      expect(comp.peliculas?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
