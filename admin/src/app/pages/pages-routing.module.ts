import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PagesComponent } from 'src/app/pages/pages.component';
import { DashboardComponent } from 'src/app/pages/dashboard/dashboard.component';
import { LoginGuard } from 'src/app/guards/login.guard';
import { LugarGuard } from 'src/app/guards/lugar.guard';
import { LugarComponent } from 'src/app/pages/lugar/lugar.component';
import { EventoComponent } from 'src/app/pages/evento/evento.component';
import { EventosComponent } from 'src/app/pages/evento/eventos.component';
import { LugarEditComponent } from 'src/app/pages/lugar/lugar-edit.component';
import { PagosComponent } from 'src/app/pages/pagos/pagos.component';
import { ReportesComponent } from 'src/app/pages/reportes/reportes.component';
import { PreferenciasComponent } from 'src/app/pages/preferencias/preferencias.component';
import { PerfilComponent } from 'src/app/pages/perfil/perfil.component';
import { RegistroComponent } from 'src/app/pages/registro/registro.component';
import { LocalizaComponent } from 'src/app/pages/localiza/localiza.component';
import { RegistradoComponent } from 'src/app/pages/registrado/registrado.component';
import { LugaresComponent } from 'src/app/pages/lugares/lugares.component';
import { LugarNoAdmComponent } from 'src/app/pages/lugar/lugar-no-adm.component';


const routes: Routes = [
  {
    path: 'pages',
    component: PagesComponent,
    children: [
        { path: 'dashboard', component: DashboardComponent, canActivate: [ LoginGuard ]},
        { path: 'localiza/:adm', component: LocalizaComponent, canActivate: [ LoginGuard ]},
        { path: 'registro', component: RegistroComponent, canActivate: [ LoginGuard ]},
        { path: 'registrado', component: RegistradoComponent, canActivate: [ LoginGuard ]},
        { path: 'pagos', component: PagosComponent, canActivate: [ LoginGuard ]},
        { path: 'reportes', component: ReportesComponent, canActivate: [ LoginGuard]},
        { path: 'perfil', component: PerfilComponent, canActivate: [ LoginGuard]},
        { path: 'preferencias', component: PreferenciasComponent, canActivate: [ LoginGuard]},
        { path: 'lugares/:adm', component: LugaresComponent, canActivate: [ LoginGuard]},
        { path: 'lugar', component: LugarComponent, canActivate: [ LoginGuard, LugarGuard ]},
        { path: 'lugarEdit', component: LugarEditComponent, canActivate: [ LoginGuard, LugarGuard ]},
        { path: 'lugarNewNA', component: LugarNoAdmComponent, canActivate: [ LoginGuard ]},
        { path: 'eventos', component: EventosComponent, canActivate: [ LoginGuard, LugarGuard ]},
        { path: 'evento/:id', component: EventoComponent, canActivate: [ LoginGuard, LugarGuard ]},
        { path: '', redirectTo: '/pages/dashboard', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule { }
  