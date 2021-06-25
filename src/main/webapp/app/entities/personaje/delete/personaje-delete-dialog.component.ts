import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonaje } from '../personaje.model';
import { PersonajeService } from '../service/personaje.service';

@Component({
  templateUrl: './personaje-delete-dialog.component.html',
})
export class PersonajeDeleteDialogComponent {
  personaje?: IPersonaje;

  constructor(protected personajeService: PersonajeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personajeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
