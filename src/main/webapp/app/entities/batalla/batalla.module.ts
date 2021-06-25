import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BatallaComponent } from './list/batalla.component';
import { BatallaDetailComponent } from './detail/batalla-detail.component';
import { BatallaUpdateComponent } from './update/batalla-update.component';
import { BatallaDeleteDialogComponent } from './delete/batalla-delete-dialog.component';
import { BatallaRoutingModule } from './route/batalla-routing.module';

@NgModule({
  imports: [SharedModule, BatallaRoutingModule],
  declarations: [BatallaComponent, BatallaDetailComponent, BatallaUpdateComponent, BatallaDeleteDialogComponent],
  entryComponents: [BatallaDeleteDialogComponent],
})
export class BatallaModule {}
