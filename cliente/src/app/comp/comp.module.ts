import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { SidebarComponent } from './sidebar/sidebar.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from 'src/app/comp/footer/footer.component';
import { PipesModule } from 'src/app/pipes/pipes.module';
import { ImagenModalComponent } from './imagen-modal/imagen-modal.component';
import { EncontruccionComponent } from './encontruccion/encontruccion.component';


@NgModule({
  declarations: [
    SidebarComponent, 
    HeaderComponent, 
    FooterComponent, ImagenModalComponent, EncontruccionComponent],
  imports: [
    CommonModule,
    RouterModule,
    PipesModule,
  ], exports: [
    SidebarComponent, 
    HeaderComponent, 
    FooterComponent,
    ImagenModalComponent,
    EncontruccionComponent
  ]
})
export class CompModule { }
