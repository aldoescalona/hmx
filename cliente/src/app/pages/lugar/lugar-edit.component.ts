import { Component, OnInit } from '@angular/core';
import { Lugar, CUDResponse } from 'src/app/model/interfaces';
import { ClienteService } from 'src/app/services/cliente.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-lugar-edit',
  templateUrl: './lugar-edit.component.html',
  styles: []
})
export class LugarEditComponent implements OnInit {

  lugar: Lugar = null;

  constructor(public clienteService: ClienteService, public router: Router) { }

  ngOnInit() {
    this.getLugar();
  }

  getLugar() {
    this.clienteService.currentLugar().subscribe((dat: Lugar) => {
      this.lugar = dat;

    });
  }


  guardarLugar() {
    this.clienteService.modificarLugar(this.lugar).subscribe((data: CUDResponse) => {
      console.log(data);
      swal('Establecimiento modificado', 'Modificado correctamente', 'success');
      this.router.navigate(['/pages/lugar']);
    });

  }
}
