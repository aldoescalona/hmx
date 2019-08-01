import { Component, OnInit } from '@angular/core';
import { Evento, CUDResponse } from 'src/app/model/interfaces';
import { Router, ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';
import { ClienteService } from 'src/app/services/cliente.service';
import { ImagenModalService } from 'src/app/comp/imagen-modal/imagen-modal.service';


@Component({
  selector: 'app-evento',
  templateUrl: './evento.component.html',
  styleUrls: ['./evento.component.css']
})
export class EventoComponent implements OnInit {

  evento: Evento = { titulo: ''};
  transiente: boolean = true;

  constructor(public clienteService: ClienteService, 
    public activatedRoute: ActivatedRoute, 
    public router: Router, 
    public imageModalService:ImagenModalService) {

      activatedRoute.params.subscribe(params => {

        let id = params['id'];
  
        console.log(id);
        if (id !== 'N') {
          this.transiente = false;
          this.cargarEvento(id);
        } else {
          this.transiente = true;
        }
  
      });

     }

  ngOnInit() {

    this.imageModalService.notificacion
    .subscribe( resp => {
      this.evento.imagenId = resp;
    });
  }

  cargarEvento(id: string) {
    this.clienteService.evento(id).subscribe((dats: Evento) => {
      this.evento = dats;
      console.log(dats);
    });
  }

  guardarEvento(f: NgForm) {

    console.log('EVENTO:', this.evento);

    if (this.transiente === true) {
      this.evento.lugarId = this.clienteService.lugar;

      this.clienteService.guardarEvento(this.evento).subscribe((data:CUDResponse) => {
        console.log(data);
        swal('Evento creado', 'Creado correctamente', 'success');
        this.router.navigate(['/pages/evento', data.id]);
      });
    } else {
      this.clienteService.modificaEvento(this.evento, this.evento.id).subscribe(data => {
        console.log(data);
        swal('Evento modificado', 'Modificado correctamente', 'success');
      });
    }
  }

  dateChanged(eventDate: string): Date | null {

    // console.log(eventDate);

    let fecha = !!eventDate ? new Date(eventDate) : null;
    // console.log(fecha);
    return fecha;
  }

  cambiarFoto(){
    this.imageModalService.mostrarModal('evento', this.evento.id);
  }

  
}
