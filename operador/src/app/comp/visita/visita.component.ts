import { Component, OnInit, EventEmitter, Input, Output } from '@angular/core';
import { Visita, RespuestaSimple } from 'src/app/model/interfaces';
import swal from 'sweetalert';
import { OperadorService } from 'src/app/services/operador.service';
import { RespuestaSimpleForm } from 'src/app/model/CredencialOp';




@Component({
  selector: 'app-visita',
  templateUrl: './visita.component.html',
  styleUrls: ['./visita.component.css']
})
export class VisitaComponent implements OnInit {

  @Input() visita:Visita;
  @Input() cargando?:boolean  = false;
  @Output('visitaCambio') visitaCambio: EventEmitter<boolean> = new EventEmitter();
  
  
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
      let simple = new RespuestaSimpleForm(this.visita.id, value);
      let res:RespuestaSimple = await this.operadorService.rechazaVisita(simple).toPromise();
      
      if(res.ok === false){
        await swal( 'Rechaza Error', res.mensaje, 'error');
      }

      this.visitaCambio.emit(true);
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
      let simple = new RespuestaSimpleForm(this.visita.id, value);
      let res:RespuestaSimple = await this.operadorService.aceptarVisita(simple).toPromise();
      
      if(res.ok === false){
        await swal( 'Aceptar Error', res.mensaje, 'error');
      }

      this.visitaCambio.emit(true);
    }   
  }


}
