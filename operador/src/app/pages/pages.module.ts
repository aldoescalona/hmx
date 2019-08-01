import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PagesComponent } from './pages.component';
import { PagesRoutingModule } from 'src/app/pages/pages-routing.module';
import { CompModule } from 'src/app/comp/comp.module';
import { PipesModule } from 'src/app/pipes/pipes.module';
import { CambiaPazwordComponent } from 'src/app/pages/cambia-pazword/cambia-pazword.component';
import { BusquedaComponent } from 'src/app/pages/busqueda/busqueda.component';
import { PerfilComponent } from './perfil/perfil.component';
import { RechazoMasivoComponent } from 'src/app/pages/rechazo-masivo/rechazo-masivo.component';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    DashboardComponent,
    BusquedaComponent,
    CambiaPazwordComponent,
    RechazoMasivoComponent,
    PagesComponent,
    PerfilComponent],
  imports: [
    CommonModule,
    FormsModule,
    PagesRoutingModule,
    CompModule,
    PipesModule
  ]
})
export class PagesModule { }
