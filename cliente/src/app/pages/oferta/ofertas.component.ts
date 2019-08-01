import { Component, OnInit, Input } from '@angular/core';
import { Lugar, Oferta } from 'src/app/model/interfaces';
import { ClienteService } from 'src/app/services/cliente.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ofertas',
  templateUrl: './ofertas.component.html',
  styleUrls: ['./ofertas.component.css']
})
export class OfertasComponent implements OnInit {

  ofertas: Oferta[] = [];
  cargando: boolean = false;

  constructor(public clienteService: ClienteService, public router: Router) { }

  ngOnInit() {
    this.cargarOfertas();
  }

  cargarOfertas() {
    this.cargando = true;
    this.clienteService.ofertas().subscribe((data: Oferta[]) => {
      this.ofertas = data;
      this.cargando = false;
    });
  }

  nuevaOferta() {
    this.router.navigate(['/pages/oferta', 'N']);
  }

  async borrarOferta(oferta: Oferta) {

    const willDelete = await swal({
      title: "Eliminar?",
      text: "¿Desea eliminar la oferta " + oferta.nombre + "?",
      icon: "warning",
      dangerMode: true,
      buttons: ["Cancelar", "Aceptar"]
    });


    if (willDelete) {
      this.clienteService.bajaOferta(oferta.id).subscribe(dats => {
        swal('Oferta eliminada', 'Eliminada correctamente', 'success');
        this.cargarOfertas();
        console.log(dats);
      });
    }
  }


  
}
