import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBatalla } from '../batalla.model';
import { BatallaService } from '../service/batalla.service';

@Component({
  templateUrl: './batalla-delete-dialog.component.html',
})
export class BatallaDeleteDialogComponent {
  batalla?: IBatalla;

  constructor(protected batallaService: BatallaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.batallaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
