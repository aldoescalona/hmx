import { Component, OnInit, EventEmitter, Input, Output } from '@angular/core';
import { Compra, RespuestaSimple } from 'src/app/model/interfaces';
import { RespuestaSimpleForm } from 'src/app/model/CredencialOp';
import { OperadorService } from 'src/app/services/operador.service';

@Component({
  selector: 'app-compra',
  templateUrl: './compra.component.html',
  styleUrls: ['./compra.component.css']
})
export class CompraComponent implements OnInit {

  @Input() compra:Compra;
  @Input() cargando?:boolean  = false;
  @Output('compraCambio') compraCambio: EventEmitter<boolean> = new EventEmitter();
  
  constructor(public operadorService: OperadorService) { }

  ngOnInit() {
  }

  async preRechazar(){
    
    const value = await swal('Motivo de rechazo', {
      content: {
        element: "input",
        attributes: {
          placeholder: "Motivo",
          type: "text",
        },
      },buttons: ["Cancelar", "Rechazar"],
      closeOnClickOutside: false,
      dangerMode: true,
      icon: "error",
      
    });

    if(value){
      let simple = new RespuestaSimpleForm(this.compra.id, value);
      let res:RespuestaSimple = await this.operadorService.rechazaCompra(simple).toPromise();
      
      if(res.ok === false){
        await swal( 'Rechaza Error', res.mensaje, 'error');
      }

      this.compraCambio.emit(true);
    }
  }

  async preAceptar(){
    
    const value = await swal('Aceptar Visita', {
      content: {
        element: "input",
        attributes: {
          placeholder: "Comentario",
          type: "text",
        },
      },buttons: ["Cancelar", "Aceptar"],
      closeOnClickOutside: false,
      icon: "success",
      
    });
        
    if(value){
      let simple = new RespuestaSimpleForm(this.compra.id, value);
      let res:RespuestaSimple = await this.operadorService.aceptarCompra(simple).toPromise();
      
      if(res.ok === false){
        await swal( 'Aceptar Error', res.mensaje, 'error');
      }

      this.compraCambio.emit(true);
    }   
  }

}
