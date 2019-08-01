import { Component, OnInit } from '@angular/core';
import { OperadorService } from 'src/app/services/operador.service';
import { DashboardOperador } from 'src/app/model/interfaces';
import { Router, ActivatedRoute } from '@angular/router';
import { Busqueda } from 'src/app/model/CredencialOp';

@Component({
  selector: 'app-busqueda',
  templateUrl: './busqueda.component.html',
  styleUrls: ['./busqueda.component.css']
})
export class BusquedaComponent implements OnInit {

  dashboard: DashboardOperador;
  cargando: boolean = false;

  private termino:string = '';

  constructor(private operadorService: OperadorService, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    // this.actualizaDashboard(null);

    this.activatedRoute.params.subscribe( params => {

      this.termino = params['termino'];
      this.actualizaDashboard(null);

    });

  }

  actualizaDashboard(event) {

    let bus:Busqueda = new Busqueda(this.termino);

    this.cargando = true;
    this.operadorService.buscar(bus).subscribe((data: DashboardOperador) => {
      this.dashboard = data;
      this.cargando = false;

    });
  }
}
