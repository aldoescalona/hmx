import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenubarComponent } from './menubar/menubar.component';
import { VisitaComponent } from './visita/visita.component';
import { CompraComponent } from './compra/compra.component';
import { PipesModule } from 'src/app/pipes/pipes.module';
import { RouterModule } from '@angular/router';
import { SubdashboardComponent } from './subdashboard/subdashboard.component';
import { FormsModule } from '@angular/forms';
import { EncontruccionComponent } from 'src/app/comp/encontruccion/encontruccion.component';


@NgModule({
  declarations: [
    MenubarComponent, 
    VisitaComponent, 
    CompraComponent, 
    SubdashboardComponent, 
    EncontruccionComponent],
  imports: [
    CommonModule,
    FormsModule,
    PipesModule,
    RouterModule
  ], exports:[
    MenubarComponent,
    VisitaComponent,
    CompraComponent,
    SubdashboardComponent,
    EncontruccionComponent
  ]
})
export class CompModule { }
