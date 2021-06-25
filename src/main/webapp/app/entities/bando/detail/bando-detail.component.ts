import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBando } from '../bando.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-bando-detail',
  templateUrl: './bando-detail.component.html',
})
export class BandoDetailComponent implements OnInit {
  bando: IBando | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bando }) => {
      this.bando = bando;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
