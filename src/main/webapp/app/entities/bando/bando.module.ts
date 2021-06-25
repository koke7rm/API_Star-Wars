import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BandoComponent } from './list/bando.component';
import { BandoDetailComponent } from './detail/bando-detail.component';
import { BandoUpdateComponent } from './update/bando-update.component';
import { BandoDeleteDialogComponent } from './delete/bando-delete-dialog.component';
import { BandoRoutingModule } from './route/bando-routing.module';

@NgModule({
  imports: [SharedModule, BandoRoutingModule],
  declarations: [BandoComponent, BandoDetailComponent, BandoUpdateComponent, BandoDeleteDialogComponent],
  entryComponents: [BandoDeleteDialogComponent],
})
export class BandoModule {}
