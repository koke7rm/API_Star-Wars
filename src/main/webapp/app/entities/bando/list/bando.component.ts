import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBando } from '../bando.model';
import { BandoService } from '../service/bando.service';
import { BandoDeleteDialogComponent } from '../delete/bando-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-bando',
  templateUrl: './bando.component.html',
})
export class BandoComponent implements OnInit {
  bandos?: IBando[];
  isLoading = false;

  constructor(protected bandoService: BandoService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bandoService.query().subscribe(
      (res: HttpResponse<IBando[]>) => {
        this.isLoading = false;
        this.bandos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBando): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(bando: IBando): void {
    const modalRef = this.modalService.open(BandoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bando = bando;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
