import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AdminService } from 'src/app/services/admin.service';
import { Lugar } from 'src/app/model/interfaces';


@Component({
  selector: 'app-lugares',
  templateUrl: './lugares.component.html',
  styleUrls: ['./lugares.component.css']
})
export class LugaresComponent implements OnInit {


  administrados:boolean = null;
  lugares:Lugar[] = [];

  constructor(public adminService: AdminService, 
    public activatedRoute: ActivatedRoute, 
    public router: Router) { 


    activatedRoute.params.subscribe(params => {

      let id = params['adm'];

      console.log(id);
      if (id === 'adm') {
        this.administrados = true;
        this.cargaLugares();
      } else {
        this.administrados = false;
        this.cargaLugaresPublicos();
      }

    });
  }

  ngOnInit() {
  }

  cargaLugares(){
    this.adminService.lugares().subscribe((dat:Lugar[])=>{
      this.lugares = dat;
    });
  }

  cargaLugaresPublicos(){
    this.adminService.lugaresPublico().subscribe((dat:Lugar[])=>{
      this.lugares = dat;
    });
  }

  nuevoLugarPublico(){
    this.router.navigate(['/pages/localiza', 'noadm']);
  }

  verLugar(lugar: Lugar) {
    this.adminService.seleccionaLugar(lugar);
    this.router.navigate(['/pages/lugar']);
  }

}
