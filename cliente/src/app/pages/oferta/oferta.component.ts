import { Component, OnInit } from '@angular/core';
import { Oferta, CUDResponse } from 'src/app/model/interfaces';
import { Router, ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';
import { ClienteService } from 'src/app/services/cliente.service';
import { ImagenModalService } from 'src/app/comp/imagen-modal/imagen-modal.service';

@Component({
  selector: 'app-oferta',
  templateUrl: './oferta.component.html',
  styleUrls: ['./oferta.component.css']
})
export class OfertaComponent implements OnInit {

  oferta: Oferta = { nombre: '', condiciones: '', descripcion: '', tipo: '' };
  transiente: boolean = true;
  cargando: boolean = false;

  constructor(public clienteService: ClienteService, 
    public activatedRoute: ActivatedRoute, 
    public router: Router, 
    public imageModalService:ImagenModalService) {

    activatedRoute.params.subscribe(params => {
            let id = params['id'];

      console.log(id);
      if (id !== 'N') {
        this.transiente = false;
        this.cargarOferta(id);
      } else {
        this.transiente = true;
      }
    });
  }

  ngOnInit() {
    
    this.imageModalService.notificacion
    .subscribe( resp => {
      this.oferta.imagenId = resp;
    });

  }

  cargarOferta(id: string) {
    this.cargando = true;
    this.clienteService.oferta(id).subscribe((dats: Oferta) => {
      this.oferta = dats;
      this.cargando = false;
    });
  }

  guardarOferta(f: NgForm) {

    if (this.transiente === true) {
      this.oferta.lugarId = this.clienteService.lugar;

      this.clienteService.guardarOferta(this.oferta).subscribe((data:CUDResponse) => {
        console.log(data);
        swal('Oferta creada', 'Creada correctamente', 'success');
        this.router.navigate(['/pages/oferta', data.id]);
      });
    } else {
      this.clienteService.modificaOferta(this.oferta, this.oferta.id).subscribe(data => {
        console.log(data);
        swal('Oferta modificada', 'Modificada correctamente', 'success');
      });
    }
  }

  cambiarFoto(){
    this.imageModalService.mostrarModal('oferta', this.oferta.id);
  }


}
