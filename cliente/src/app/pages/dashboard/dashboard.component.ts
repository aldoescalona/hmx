import { Component, OnInit } from '@angular/core';
import { ClienteService } from 'src/app/services/cliente.service';
import { Lugar } from 'src/app/model/interfaces';
import { Router } from '@angular/router';
import { ImagenModalService } from 'src/app/comp/imagen-modal/imagen-modal.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  lugares: Lugar[] = [];
  cargando: boolean = false;

  constructor(
    private clienteService: ClienteService,
    public router: Router,
    public imageModalService: ImagenModalService) { }

  ngOnInit() {
    this.actualizaLugares();
    this.clienteService.seleccionaLugar(null);
  }

  actualizaLugares() {
    this.cargando = true;
    this.clienteService.lugares().subscribe((data: Lugar[]) => {
      this.lugares = data;
      this.cargando = false;
    });
  }

  verLugar(lugar: Lugar) {
    this.clienteService.seleccionaLugar(lugar);
    this.router.navigate(['/pages/lugar']);
  }

  verModal() {
    this.imageModalService.mostrarModal('evento', 2);
  }


}
