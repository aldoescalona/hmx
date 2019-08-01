import { Component, OnInit } from '@angular/core';
import { Operador } from 'src/app/model/interfaces';
import { ClienteService } from 'src/app/services/cliente.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-operadores',
  templateUrl: './operadores.component.html',
  styleUrls: ['./operadores.component.css']
})
export class OperadoresComponent implements OnInit {

  operadores: Operador[] = [];
  constructor(public clienteService: ClienteService, public router: Router) { }

  ngOnInit() {
    this.cargarOperadores();
  }

  cargarOperadores() {
    this.clienteService.operadores().subscribe((data: Operador[]) => {
      this.operadores = data;
      console.log(this.operadores);
    });
  }

  nuevoOperador() {
    this.router.navigate(['/pages/operador', 'N']);
  }

  async borrarOperador(operador: Operador) {

    const willDelete = await swal({
      title: "Eliminar?",
      text: "¿Desea eliminar la operador " + operador.nombre + "?",
      icon: "warning",
      dangerMode: true,
      buttons: ["Cancelar", "Aceptar"]
    });


    if (willDelete) {
      this.clienteService.bajaOperador(operador.id).subscribe(dats => {
        swal('Operador eliminado', 'Eliminado correctamente', 'success');
        this.cargarOperadores();
        console.log(dats);
      });
    }
  }

  async cambiaContrasena(operador: Operador){

    
    const value = await swal('Ingrese su nueva contraseña', {
      content: {
        element: "input",
        attributes: {
          placeholder: "Contraseña",
          type: "password",
        },
      },buttons: ["Cancelar", "Aceptar"],
      closeOnClickOutside: false,
      dangerMode: false,
      icon: "warning",
      
    });

    if(value){
      console.log(value);
      operador.contrasena = value;
      let res: any = await this.clienteService.cambiaPassOperador(operador).toPromise();

      if(res.ok === false){
        await swal( 'Fallo cambio de contraseña', res.mensaje, 'error');
      }
    }
  }

  
}
