import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BatallaDetailComponent } from './batalla-detail.component';

describe('Component Tests', () => {
  describe('Batalla Management Detail Component', () => {
    let comp: BatallaDetailComponent;
    let fixture: ComponentFixture<BatallaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BatallaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ batalla: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BatallaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BatallaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load batalla on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.batalla).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
