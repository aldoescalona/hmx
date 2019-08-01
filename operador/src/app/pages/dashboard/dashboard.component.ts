import { Component, OnInit } from '@angular/core';
import { OperadorService } from 'src/app/services/operador.service';
import { DashboardOperador } from 'src/app/model/interfaces';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  dashboard:DashboardOperador;
  // public modalToggle: string = 'oculto';

  cargando:boolean = false;

  constructor(private operadorService:OperadorService) { }

  ngOnInit() {
    this.actualizaDashboard(null);

  }

  actualizaDashboard(event){
    
    this.cargando = true;
    this.operadorService.dashboard().subscribe((data:DashboardOperador)=>{
      this.dashboard = data;
      this.cargando = false;

    });
  }
}
