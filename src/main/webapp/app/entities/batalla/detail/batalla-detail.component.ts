import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBatalla } from '../batalla.model';

@Component({
  selector: 'jhi-batalla-detail',
  templateUrl: './batalla-detail.component.html',
})
export class BatallaDetailComponent implements OnInit {
  batalla: IBatalla | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ batalla }) => {
      this.batalla = batalla;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
