import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBatalla } from '../batalla.model';
import { BatallaService } from '../service/batalla.service';
import { BatallaDeleteDialogComponent } from '../delete/batalla-delete-dialog.component';

@Component({
  selector: 'jhi-batalla',
  templateUrl: './batalla.component.html',
})
export class BatallaComponent implements OnInit {
  batallas?: IBatalla[];
  isLoading = false;

  constructor(protected batallaService: BatallaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.batallaService.query().subscribe(
      (res: HttpResponse<IBatalla[]>) => {
        this.isLoading = false;
        this.batallas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBatalla): number {
    return item.id!;
  }

  delete(batalla: IBatalla): void {
    const modalRef = this.modalService.open(BatallaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.batalla = batalla;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
