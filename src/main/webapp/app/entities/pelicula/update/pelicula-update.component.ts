import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPelicula, Pelicula } from '../pelicula.model';
import { PeliculaService } from '../service/pelicula.service';
import { IPersonaje } from 'app/entities/personaje/personaje.model';
import { PersonajeService } from 'app/entities/personaje/service/personaje.service';

@Component({
  selector: 'jhi-pelicula-update',
  templateUrl: './pelicula-update.component.html',
})
export class PeliculaUpdateComponent implements OnInit {
  isSaving = false;

  personajesSharedCollection: IPersonaje[] = [];

  editForm = this.fb.group({
    id: [],
    titulo: [null, []],
    episodio: [],
    estreno: [],
    personajes: [],
  });

  constructor(
    protected peliculaService: PeliculaService,
    protected personajeService: PersonajeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pelicula }) => {
      this.updateForm(pelicula);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pelicula = this.createFromForm();
    if (pelicula.id !== undefined) {
      this.subscribeToSaveResponse(this.peliculaService.update(pelicula));
    } else {
      this.subscribeToSaveResponse(this.peliculaService.create(pelicula));
    }
  }

  trackPersonajeById(index: number, item: IPersonaje): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPelicula>>): void {
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

  protected updateForm(pelicula: IPelicula): void {
    this.editForm.patchValue({
      id: pelicula.id,
      titulo: pelicula.titulo,
      episodio: pelicula.episodio,
      estreno: pelicula.estreno,
      personajes: pelicula.personajes,
    });

    this.personajesSharedCollection = this.personajeService.addPersonajeToCollectionIfMissing(
      this.personajesSharedCollection,
      ...(pelicula.personajes ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personajeService
      .query()
      .pipe(map((res: HttpResponse<IPersonaje[]>) => res.body ?? []))
      .pipe(
        map((personajes: IPersonaje[]) =>
          this.personajeService.addPersonajeToCollectionIfMissing(personajes, ...(this.editForm.get('personajes')!.value ?? []))
        )
      )
      .subscribe((personajes: IPersonaje[]) => (this.personajesSharedCollection = personajes));
  }

  protected createFromForm(): IPelicula {
    return {
      ...new Pelicula(),
      id: this.editForm.get(['id'])!.value,
      titulo: this.editForm.get(['titulo'])!.value,
      episodio: this.editForm.get(['episodio'])!.value,
      estreno: this.editForm.get(['estreno'])!.value,
      personajes: this.editForm.get(['personajes'])!.value,
    };
  }
}
