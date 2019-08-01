import { Component, OnInit, EventEmitter } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
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

  

  constructor(public adminService: AdminService, private router: Router) {

  }

  ngOnInit() {

    if (this.cliente === null) {

      this.cargandoCliente = true;
      this.adminService.adminFirmado().subscribe((cte: Cliente) => {
        this.cliente = cte;
        this.cargandoCliente = false;
      });
    }

    this.adminService.notificacion.subscribe(dat=>{
      console.log(dat);
    });
  }

  irDashboard(){
    this.adminService.seleccionaLugar(null);
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
    this.adminService.logout();
    this.router.navigate(['/login']);
  }

}
