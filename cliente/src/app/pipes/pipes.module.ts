import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImagenPipe } from 'src/app/pipes/imagen.pipe';
import { RecursoPipe } from 'src/app/pipes/recurso.pipe';

@NgModule({
  declarations: [ImagenPipe, RecursoPipe],
  imports: [
    CommonModule
  ],
  exports:[ImagenPipe, RecursoPipe]
})
export class PipesModule { }
