import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PagesComponent } from 'src/app/pages/pages.component';
import { DashboardComponent } from 'src/app/pages/dashboard/dashboard.component';
import { LoginGuard } from 'src/app/guards/login.guard';
import { LugarGuard } from 'src/app/guards/lugar.guard';
import { LugarComponent } from 'src/app/pages/lugar/lugar.component';
import { OfertaComponent } from 'src/app/pages/oferta/oferta.component';
import { OfertasComponent } from 'src/app/pages/oferta/ofertas.component';
import { EventoComponent } from 'src/app/pages/evento/evento.component';
import { EventosComponent } from 'src/app/pages/evento/eventos.component';
import { OperadorComponent } from 'src/app/pages/operador/operador.component';
import { OperadoresComponent } from 'src/app/pages/operador/operadores.component';
import { LugarEditComponent } from 'src/app/pages/lugar/lugar-edit.component';
import { PagosComponent } from 'src/app/pages/pagos/pagos.component';
import { ReportesComponent } from 'src/app/pages/reportes/reportes.component';
import { PreferenciasComponent } from 'src/app/pages/preferencias/preferencias.component';
import { PerfilComponent } from 'src/app/pages/perfil/perfil.component';


const routes: Routes = [
  {
    path: 'pages',
    component: PagesComponent,
    children: [
        { path: 'dashboard', component: DashboardComponent, canActivate: [ LoginGuard ]},
        { path: 'pagos', component: PagosComponent, canActivate: [ LoginGuard ]},
        { path: 'reportes', component: ReportesComponent, canActivate: [ LoginGuard]},
        { path: 'perfil', component: PerfilComponent, canActivate: [ LoginGuard]},
        { path: 'preferencias', component: PreferenciasComponent, canActivate: [ LoginGuard]},
        { path: 'lugar', component: LugarComponent, canActivate: [ LoginGuard, LugarGuard ]},
        { path: 'lugarEdit', component: LugarEditComponent, canActivate: [ LoginGuard, LugarGuard ]},
        { path: 'ofertas', component: OfertasComponent, canActivate: [ LoginGuard, LugarGuard ]},
        { path: 'oferta/:id', component: OfertaComponent, canActivate: [ LoginGuard, LugarGuard ]},
        { path: 'eventos', component: EventosComponent, canActivate: [ LoginGuard, LugarGuard ]},
        { path: 'evento/:id', component: EventoComponent, canActivate: [ LoginGuard, LugarGuard ]},
        { path: 'operadores', component: OperadoresComponent, canActivate: [ LoginGuard, LugarGuard ]},
        { path: 'operador/:id', component: OperadorComponent, canActivate: [ LoginGuard, LugarGuard ]},
        { path: '', redirectTo: '/pages/dashboard', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule { }
  