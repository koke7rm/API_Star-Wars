import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPelicula } from '../pelicula.model';
import { PeliculaService } from '../service/pelicula.service';
import { PeliculaDeleteDialogComponent } from '../delete/pelicula-delete-dialog.component';

@Component({
  selector: 'jhi-pelicula',
  templateUrl: './pelicula.component.html',
})
export class PeliculaComponent implements OnInit {
  peliculas?: IPelicula[];
  isLoading = false;

  constructor(protected peliculaService: PeliculaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.peliculaService.query().subscribe(
      (res: HttpResponse<IPelicula[]>) => {
        this.isLoading = false;
        this.peliculas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPelicula): number {
    return item.id!;
  }

  delete(pelicula: IPelicula): void {
    const modalRef = this.modalService.open(PeliculaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pelicula = pelicula;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
