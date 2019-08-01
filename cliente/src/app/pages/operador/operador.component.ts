import { Component, OnInit } from '@angular/core';
import { Operador, CUDResponse } from 'src/app/model/interfaces';
import { ClienteService } from 'src/app/services/cliente.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgForm } from '@angular/forms';


@Component({
  selector: 'app-operador',
  templateUrl: './operador.component.html',
  styleUrls: ['./operador.component.css']
})
export class OperadorComponent implements OnInit {

  operador: Operador = { nombre: '', clave: '' };
  transiente: boolean = true;

  constructor(public clienteService: ClienteService,
    public activatedRoute: ActivatedRoute,
    public router: Router) {
    activatedRoute.params.subscribe(params => {

      let id = params['id'];

      console.log(id);
      if (id !== 'N') {
        this.transiente = false;
        this.cargarOperador(id);
      } else {
        this.transiente = true;
      }

    });

  }

  ngOnInit() {
  }

  cargarOperador(id: string) {
    this.clienteService.operador(id).subscribe((dats: Operador) => {
      this.operador = dats;
      console.log(dats);
    });
  }

  guardarOperador(f: NgForm) {

    if (this.transiente === true) {
      this.operador.lugarId = this.clienteService.lugar;

      this.clienteService.guardarOperador(this.operador).subscribe((data: CUDResponse) => {
        console.log(data);
        // swal('Operador creado', 'Creado corre tamente', 'success');
        this.router.navigate(['/pages/operadores']);
      });
    } else {
      this.clienteService.modificaOperador(this.operador, this.operador.id).subscribe(data => {
        console.log(data);
        this.router.navigate(['/pages/operadores']);
        // swal('Operador modificado', 'Modificado correctamente', 'success');
      });
    }
  }


 
}
