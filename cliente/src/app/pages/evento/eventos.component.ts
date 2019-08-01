import { Component, OnInit } from '@angular/core';
import { Evento } from 'src/app/model/interfaces';
import { ClienteService } from 'src/app/services/cliente.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-eventos',
  templateUrl: './eventos.component.html',
  styleUrls: ['./eventos.component.css']
})
export class EventosComponent implements OnInit {

  eventos: Evento[] = [];
  constructor(public clienteService: ClienteService, public router: Router) { }

  ngOnInit() {
    this.cargarEventos();
  }

  cargarEventos() {
    this.clienteService.eventos().subscribe((data: Evento[]) => {
      this.eventos = data;
      console.log(this.eventos);
    });
  }

  nuevoEvento() {
    this.router.navigate(['/pages/evento', 'N']);
  }

  async borrarEvento(evento: Evento) {

    const willDelete = await swal({
      title: "Eliminar?",
      text: "¿Desea eliminar el evento " + evento.titulo + "?",
      icon: "warning",
      dangerMode: true,
      buttons: ["Cancelar", "Aceptar"]
    });


    if (willDelete) {
      this.clienteService.bajaEvento(evento.id).subscribe(dats => {
        swal('Evento eliminado', 'Eliminado correctamente', 'success');
        this.cargarEventos();
        console.log(dats);
      });
    }
  }

  
}
