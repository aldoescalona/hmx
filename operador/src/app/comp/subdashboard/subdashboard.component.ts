import { Component, OnInit, EventEmitter, Input, Output } from '@angular/core';
import { DashboardOperador } from 'src/app/model/interfaces';

@Component({
  selector: 'app-subdashboard',
  templateUrl: './subdashboard.component.html',
  styleUrls: ['./subdashboard.component.css']
})
export class SubdashboardComponent implements OnInit {

  @Input() dashboard:DashboardOperador;
  @Input() cargando?:boolean  = false;
  @Output('dashboardCambio') dashboardCambio: EventEmitter<boolean> = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  actualizaSubdashboard(event?){
    this.dashboardCambio.emit(true);
  }

}
