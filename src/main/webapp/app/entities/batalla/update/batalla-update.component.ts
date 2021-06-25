import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBatalla, Batalla } from '../batalla.model';
import { BatallaService } from '../service/batalla.service';
import { IPersonaje } from 'app/entities/personaje/personaje.model';
import { PersonajeService } from 'app/entities/personaje/service/personaje.service';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { PeliculaService } from 'app/entities/pelicula/service/pelicula.service';
import { IBando } from 'app/entities/bando/bando.model';
import { BandoService } from 'app/entities/bando/service/bando.service';

@Component({
  selector: 'jhi-batalla-update',
  templateUrl: './batalla-update.component.html',
})
export class BatallaUpdateComponent implements OnInit {
  isSaving = false;

  personajesSharedCollection: IPersonaje[] = [];
  peliculasSharedCollection: IPelicula[] = [];
  bandosSharedCollection: IBando[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, []],
    planeta: [],
    involucrados: [],
    pelicula: [],
    ganador: [],
    perdedor: [],
  });

  constructor(
    protected batallaService: BatallaService,
    protected personajeService: PersonajeService,
    protected peliculaService: PeliculaService,
    protected bandoService: BandoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ batalla }) => {
      this.updateForm(batalla);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const batalla = this.createFromForm();
    if (batalla.id !== undefined) {
      this.subscribeToSaveResponse(this.batallaService.update(batalla));
    } else {
      this.subscribeToSaveResponse(this.batallaService.create(batalla));
    }
  }

  trackPersonajeById(index: number, item: IPersonaje): number {
    return item.id!;
  }

  trackPeliculaById(index: number, item: IPelicula): number {
    return item.id!;
  }

  trackBandoById(index: number, item: IBando): number {
    return item.id!;
  }

  getSelectedPersonaje(option: IPersonaje, selectedVals?: IPersonaje[]): IPersonaje {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBatalla>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(batalla: IBatalla): void {
    this.editForm.patchValue({
      id: batalla.id,
      nombre: batalla.nombre,
      planeta: batalla.planeta,
      involucrados: batalla.involucrados,
      pelicula: batalla.pelicula,
      ganador: batalla.ganador,
      perdedor: batalla.perdedor,
    });

    this.personajesSharedCollection = this.personajeService.addPersonajeToCollectionIfMissing(
      this.personajesSharedCollection,
      ...(batalla.involucrados ?? [])
    );
    this.peliculasSharedCollection = this.peliculaService.addPeliculaToCollectionIfMissing(
      this.peliculasSharedCollection,
      batalla.pelicula
    );
    this.bandosSharedCollection = this.bandoService.addBandoToCollectionIfMissing(
      this.bandosSharedCollection,
      batalla.ganador,
      batalla.perdedor
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personajeService
      .query()
      .pipe(map((res: HttpResponse<IPersonaje[]>) => res.body ?? []))
      .pipe(
        map((personajes: IPersonaje[]) =>
          this.personajeService.addPersonajeToCollectionIfMissing(personajes, ...(this.editForm.get('involucrados')!.value ?? []))
        )
      )
      .subscribe((personajes: IPersonaje[]) => (this.personajesSharedCollection = personajes));

    this.peliculaService
      .query()
      .pipe(map((res: HttpResponse<IPelicula[]>) => res.body ?? []))
      .pipe(
        map((peliculas: IPelicula[]) =>
          this.peliculaService.addPeliculaToCollectionIfMissing(peliculas, this.editForm.get('pelicula')!.value)
        )
      )
      .subscribe((peliculas: IPelicula[]) => (this.peliculasSharedCollection = peliculas));

    this.bandoService
      .query()
      .pipe(map((res: HttpResponse<IBando[]>) => res.body ?? []))
      .pipe(
        map((bandos: IBando[]) =>
          this.bandoService.addBandoToCollectionIfMissing(bandos, this.editForm.get('ganador')!.value, this.editForm.get('perdedor')!.value)
        )
      )
      .subscribe((bandos: IBando[]) => (this.bandosSharedCollection = bandos));
  }

  protected createFromForm(): IBatalla {
    return {
      ...new Batalla(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      planeta: this.editForm.get(['planeta'])!.value,
      involucrados: this.editForm.get(['involucrados'])!.value,
      pelicula: this.editForm.get(['pelicula'])!.value,
      ganador: this.editForm.get(['ganador'])!.value,
      perdedor: this.editForm.get(['perdedor'])!.value,
    };
  }
}
