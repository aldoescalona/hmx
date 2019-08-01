import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PagesComponent } from './pages.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PagesRoutingModule } from 'src/app/pages/pages-routing.module';
import { CompModule } from 'src/app/comp/comp.module';
import { PipesModule } from 'src/app/pipes/pipes.module';
import { LugarComponent } from 'src/app/pages/lugar/lugar.component';
import { OfertaComponent } from './oferta/oferta.component';
import { FormsModule } from '@angular/forms';
import { OfertasComponent } from './oferta/ofertas.component';
import { EventoComponent } from './evento/evento.component';
import { OperadorComponent } from './operador/operador.component';
import { EventosComponent } from './evento/eventos.component';
import { OperadoresComponent } from './operador/operadores.component';
import { LugarEditComponent } from './lugar/lugar-edit.component';
import { PagosComponent } from './pagos/pagos.component';
import { ReportesComponent } from './reportes/reportes.component';
import { PerfilComponent } from './perfil/perfil.component';
import { PreferenciasComponent } from './preferencias/preferencias.component';


@NgModule({
  declarations: [
    PagesComponent, 
    DashboardComponent,
    LugarComponent,
    OfertaComponent,
    OfertasComponent,
    EventoComponent,
    OperadorComponent,
    EventosComponent,
    OperadoresComponent,
    LugarEditComponent,
    PagosComponent,
    ReportesComponent,
    PerfilComponent,
    PreferenciasComponent
  ],
  imports: [
    CommonModule,
    PagesRoutingModule,
    FormsModule,
    PipesModule,
    CompModule
  ]
})
export class PagesModule { }
