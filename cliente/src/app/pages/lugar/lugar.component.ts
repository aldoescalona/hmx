import { Component, OnInit, Input } from '@angular/core';
import { Lugar, Oferta } from 'src/app/model/interfaces';
import { ClienteService } from 'src/app/services/cliente.service';
import { Router } from '@angular/router';
import { ImagenModalService } from 'src/app/comp/imagen-modal/imagen-modal.service';

declare var $: any;

@Component({
  selector: 'app-lugar',
  templateUrl: './lugar.component.html',
  styleUrls: ['./lugar.component.css']
})
export class LugarComponent implements OnInit {

  lugar: Lugar = null;

  constructor(public clienteService: ClienteService, 
    public router: Router,
    public imageModalService:ImagenModalService) { }

  ngOnInit() {
    this.imageModalService.notificacion
    .subscribe( resp => {
      this.getLugar();
      // this.evento.imagenId = resp;
    });
    this.getLugar();
  }

  getLugar() {
    this.clienteService.currentLugar().subscribe((dat: Lugar) => {
      this.lugar = dat;
    });
  }

  editarLugar() {
    this.router.navigate(['/pages/lugarEdit']);
  }
  
  cambiarFotoLogo(){
    this.imageModalService.mostrarModal('lugar-logo', this.lugar.id);
  }

  cambiarFotoPortada(){
    this.imageModalService.mostrarModal('lugar-portada', this.lugar.id);
  }
}
