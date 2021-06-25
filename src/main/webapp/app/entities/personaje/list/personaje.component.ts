import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonaje } from '../personaje.model';
import { PersonajeService } from '../service/personaje.service';
import { PersonajeDeleteDialogComponent } from '../delete/personaje-delete-dialog.component';

@Component({
  selector: 'jhi-personaje',
  templateUrl: './personaje.component.html',
})
export class PersonajeComponent implements OnInit {
  personajes?: IPersonaje[];
  isLoading = false;

  constructor(protected personajeService: PersonajeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.personajeService.query().subscribe(
      (res: HttpResponse<IPersonaje[]>) => {
        this.isLoading = false;
        this.personajes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPersonaje): number {
    return item.id!;
  }

  delete(personaje: IPersonaje): void {
    const modalRef = this.modalService.open(PersonajeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.personaje = personaje;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
