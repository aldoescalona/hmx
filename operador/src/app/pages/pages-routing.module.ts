import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PagesComponent } from 'src/app/pages/pages.component';
import { DashboardComponent } from 'src/app/pages/dashboard/dashboard.component';

import { LoginGuard } from 'src/app/guards/login.guard';
import { CambiaPazwordComponent } from 'src/app/pages/cambia-pazword/cambia-pazword.component';
import { BusquedaComponent } from 'src/app/pages/busqueda/busqueda.component';
import { RechazoMasivoComponent } from 'src/app/pages/rechazo-masivo/rechazo-masivo.component';
import { PerfilComponent } from 'src/app/pages/perfil/perfil.component';


const routes: Routes = [
  {
    path: 'pages',
    component: PagesComponent,
    children: [
      { path: 'dashboard', component: DashboardComponent, canActivate: [ LoginGuard ]},
      { path: 'busqueda', component: BusquedaComponent, canActivate: [ LoginGuard ]},
      { path: 'rechazo', component: RechazoMasivoComponent, canActivate: [ LoginGuard ]},
      { path: 'cambiapaz', component: CambiaPazwordComponent, canActivate: [ LoginGuard ]},
      { path: 'perfil', component: PerfilComponent, canActivate: [ LoginGuard ]},
      { path: '', redirectTo: '/pages/dashboard', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule { }
