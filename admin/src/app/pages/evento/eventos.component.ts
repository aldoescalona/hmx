import { Component, OnInit } from '@angular/core';
import { Evento } from 'src/app/model/interfaces';
import { AdminService } from 'src/app/services/admin.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-eventos',
  templateUrl: './eventos.component.html',
  styleUrls: ['./eventos.component.css']
})
export class EventosComponent implements OnInit {

  eventos: Evento[] = [];
  constructor(public adminService: AdminService, public router: Router) { }

  ngOnInit() {
    this.cargarEventos();
  }

  cargarEventos() {
    this.adminService.eventos().subscribe((data: Evento[]) => {
      this.eventos = data;
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
      this.adminService.bajaEvento(evento.id).subscribe(dats => {
        swal('Evento eliminado', 'Eliminado correctamente', 'success');
        this.cargarEventos();
        console.log(dats);
      });
    }
  }

  
}
