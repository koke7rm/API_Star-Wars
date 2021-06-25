import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPersonaje, Personaje } from '../personaje.model';
import { PersonajeService } from '../service/personaje.service';

@Component({
  selector: 'jhi-personaje-update',
  templateUrl: './personaje-update.component.html',
})
export class PersonajeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, []],
    edad: [],
    rango: [],
    especie: [],
  });

  constructor(protected personajeService: PersonajeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personaje }) => {
      this.updateForm(personaje);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personaje = this.createFromForm();
    if (personaje.id !== undefined) {
      this.subscribeToSaveResponse(this.personajeService.update(personaje));
    } else {
      this.subscribeToSaveResponse(this.personajeService.create(personaje));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonaje>>): void {
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

  protected updateForm(personaje: IPersonaje): void {
    this.editForm.patchValue({
      id: personaje.id,
      nombre: personaje.nombre,
      edad: personaje.edad,
      rango: personaje.rango,
      especie: personaje.especie,
    });
  }

  protected createFromForm(): IPersonaje {
    return {
      ...new Personaje(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      edad: this.editForm.get(['edad'])!.value,
      rango: this.editForm.get(['rango'])!.value,
      especie: this.editForm.get(['especie'])!.value,
    };
  }
}
