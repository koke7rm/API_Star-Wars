import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonaje } from '../personaje.model';

@Component({
  selector: 'jhi-personaje-detail',
  templateUrl: './personaje-detail.component.html',
})
export class PersonajeDetailComponent implements OnInit {
  personaje: IPersonaje | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personaje }) => {
      this.personaje = personaje;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
