import { Injectable } from '@angular/core';
import { CodigoPostal, FichaRegistroRespuesta, FichaRegistro } from 'src/app/model/interfaces';

@Injectable({
  providedIn: 'root'
})
export class RegistroService {

  public administrado:boolean = true;  
  public codigoPostal:CodigoPostal = null;
  public ficha:FichaRegistro = null;
  public respuesta:FichaRegistroRespuesta = null;

  constructor() { }
 

}
