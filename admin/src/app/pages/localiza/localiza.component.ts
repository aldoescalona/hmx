import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
import { CodigoPostal } from 'src/app/model/interfaces';
import { Router, ActivatedRoute } from '@angular/router';
import { RegistroService } from 'src/app/services/registro.service';

@Component({
  selector: 'app-localiza',
  templateUrl: './localiza.component.html',
  styleUrls: ['./localiza.component.css']
})
export class LocalizaComponent implements OnInit {

  administrados:boolean = null;
  cp:string = '';
  codigos:CodigoPostal[] = [];

  constructor(public adminService: AdminService, 
    public registroService: RegistroService,
    public activatedRoute: ActivatedRoute, 
    public router: Router) { 


    activatedRoute.params.subscribe(params => {

      let id = params['adm'];

      if (id === 'adm') {
        this.administrados = true;
        registroService.administrado = true;
      } else {
        this.administrados = false;
        registroService.administrado = false; 
      }

    });
  }

  ngOnInit() {
  }


  buscarCP(){
    this.adminService.cps(this.cp).subscribe((dat:CodigoPostal[])=>{
      console.log(dat);
      this.codigos = dat;
    });
  }

  preparaRegistro(codigoPostal:CodigoPostal){
    this.registroService.codigoPostal = codigoPostal;

    if(this.administrados){
     this.router.navigate(['/pages/registro']);
    }else {
      this.router.navigate(['pages/lugarNewNA']);
    }
  }
}
