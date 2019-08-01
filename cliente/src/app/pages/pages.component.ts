import { Component, OnInit, EventEmitter } from '@angular/core';
import { ClienteService } from 'src/app/services/cliente.service';
import { Router } from '@angular/router';
import { Cliente } from 'src/app/model/interfaces';

@Component({
  selector: 'app-pages',
  templateUrl: './pages.component.html',
  styleUrls: ['./pages.component.css']
})
export class PagesComponent implements OnInit {

  toggleClass = '';
  cliente: Cliente = null;

  cargandoCliente: boolean = false

  

  constructor(public clienteService: ClienteService, private router: Router) {

  }

  ngOnInit() {

    if (this.cliente === null) {

      this.cargandoCliente = true;
      this.clienteService.clienteFirmado().subscribe((cte: Cliente) => {
        this.cliente = cte;
        this.cargandoCliente = false;
      });
    }

    this.clienteService.notificacion.subscribe(dat=>{
      console.log(dat);
    });
  }

  irDashboard(){
    this.clienteService.seleccionaLugar(null);
    this.router.navigate(['/pages/dashboard']);
  }

  toggleMenu() {

    console.log(this.toggleClass);

    if (this.toggleClass === 'active') {
      this.toggleClass = '';
    } else {
      this.toggleClass = 'active';
    }
  }

  salir() {
    this.clienteService.logout();
    this.router.navigate(['/login']);
  }

}
