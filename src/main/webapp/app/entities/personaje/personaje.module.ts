import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PersonajeComponent } from './list/personaje.component';
import { PersonajeDetailComponent } from './detail/personaje-detail.component';
import { PersonajeUpdateComponent } from './update/personaje-update.component';
import { PersonajeDeleteDialogComponent } from './delete/personaje-delete-dialog.component';
import { PersonajeRoutingModule } from './route/personaje-routing.module';

@NgModule({
  imports: [SharedModule, PersonajeRoutingModule],
  declarations: [PersonajeComponent, PersonajeDetailComponent, PersonajeUpdateComponent, PersonajeDeleteDialogComponent],
  entryComponents: [PersonajeDeleteDialogComponent],
})
export class PersonajeModule {}
