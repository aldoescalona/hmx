import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PagesComponent } from './pages.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PagesRoutingModule } from 'src/app/pages/pages-routing.module';
import { CompModule } from 'src/app/comp/comp.module';
import { PipesModule } from 'src/app/pipes/pipes.module';
import { LugarComponent } from 'src/app/pages/lugar/lugar.component';
import { FormsModule } from '@angular/forms';
import { EventoComponent } from './evento/evento.component';
import { EventosComponent } from './evento/eventos.component';
import { LugarEditComponent } from './lugar/lugar-edit.component';
import { PagosComponent } from './pagos/pagos.component';
import { ReportesComponent } from './reportes/reportes.component';
import { PerfilComponent } from './perfil/perfil.component';
import { PreferenciasComponent } from './preferencias/preferencias.component';
import { RegistroComponent } from './registro/registro.component';
import { LocalizaComponent } from './localiza/localiza.component';
import { RegistradoComponent } from './registrado/registrado.component';
import { CarteleraComponent } from './cartelera/cartelera.component';
import { LugaresComponent } from './lugares/lugares.component';
import { LugarNoAdmComponent } from './lugar/lugar-no-adm.component';


@NgModule({
  declarations: [
    PagesComponent, 
    DashboardComponent,
    RegistroComponent,
    LugarComponent,
    EventoComponent,
    EventosComponent,
    LugarEditComponent,
    PagosComponent,
    ReportesComponent,
    PerfilComponent,
    PreferenciasComponent,
    LocalizaComponent,
    RegistradoComponent,
    CarteleraComponent,
    LugaresComponent,
    LugarNoAdmComponent
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
