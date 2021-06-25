import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBando } from '../bando.model';
import { BandoService } from '../service/bando.service';

@Component({
  templateUrl: './bando-delete-dialog.component.html',
})
export class BandoDeleteDialogComponent {
  bando?: IBando;

  constructor(protected bandoService: BandoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bandoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
