import { Component, OnInit } from '@angular/core';
import { DashboardOperador, Visita, RespuestaSimple } from 'src/app/model/interfaces';
import { OperadorService } from 'src/app/services/operador.service';
import { RespuestaSimpleForm } from 'src/app/model/CredencialOp';

@Component({
  selector: 'app-rechazo-masivo',
  templateUrl: './rechazo-masivo.component.html',
  styleUrls: ['./rechazo-masivo.component.css']
})
export class RechazoMasivoComponent implements OnInit {

  visitas: Visita[] = [];
  private visitasChecked: Visita[] = [];
  cargando: boolean = false;
  masterSelected: boolean;

  constructor(private operadorService: OperadorService) { }

  ngOnInit() {
    this.actualizaDashboard();
  }

  actualizaDashboard() {

    this.cargando = true;
    this.operadorService.visitasPendientes().subscribe((data: DashboardOperador) => {
      this.visitas = data.visitas;
      this.cargando = false;

    });
  }

  checkUncheckAll() {
    for (var i = 0; i < this.visitas.length; i++) {
      this.visitas[i].sel = this.masterSelected;
    }
    // this.getCheckedItemList();
  }
  isAllSelected() {
    this.masterSelected = this.visitas.every(function (item: any) {
      return item.isSelected == true;
    })
    // this.getCheckedItemList();
  }

  getCheckedItemList() {
    this.visitasChecked = [];
    for (var i = 0; i < this.visitas.length; i++) {
      if (this.visitas[i].sel)
        this.visitasChecked.push(this.visitas[i]);
    }

  }


  async preRechazar() {

    this.getCheckedItemList();
    const value = await swal('Motivo de rechazo', {
      content: {
        element: "input",
        attributes: {
          placeholder: "Motivo",
          type: "text",
        },
      }, buttons: ["Cancelar", "Rechazar"],
      closeOnClickOutside: false,
      dangerMode: true,
      icon: "error",

    });

    if (value) {


      for (let i = 0; i < this.visitasChecked.length; i++) {
        let vis: Visita = this.visitasChecked[i];

        let simple = new RespuestaSimpleForm(vis.id, value);
        let res: RespuestaSimple = await this.operadorService.rechazaVisita(simple).toPromise();
        console.log(res);
      }

      this.actualizaDashboard();
    }
  }

}
