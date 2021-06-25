import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BandoService } from '../service/bando.service';

import { BandoComponent } from './bando.component';

describe('Component Tests', () => {
  describe('Bando Management Component', () => {
    let comp: BandoComponent;
    let fixture: ComponentFixture<BandoComponent>;
    let service: BandoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BandoComponent],
      })
        .overrideTemplate(BandoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BandoComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BandoService);

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
      expect(comp.bandos?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
