import { Injectable, EventEmitter } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ImagenModalService {

  public oculto: string = 'oculto';
  public tipo: string;
  public id: number;

  public notificacion = new EventEmitter<any>();


  constructor() { }


  ocultarModal() {
    this.oculto = 'oculto';
    this.tipo = null;
    this.id = null;
  }

  mostrarModal( tipo: string, id: number ) {
    this.oculto = '';
    this.id = id;
    this.tipo = tipo;
  }
}
