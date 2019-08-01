import { Component, OnInit } from '@angular/core';
import { Lugar, CUDResponse } from 'src/app/model/interfaces';
import { NgForm } from '@angular/forms';
import { AdminService } from 'src/app/services/admin.service';
import { RegistroService } from 'src/app/services/registro.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-lugar-no-adm',
  templateUrl: './lugar-no-adm.component.html',
  styleUrls: ['./lugar-no-adm.component.css']
})
export class LugarNoAdmComponent implements OnInit {

  lugar:Lugar = null;

  constructor(private adminService: AdminService, 
    private registroService:RegistroService,
    public router: Router) {
   
  }

  ngOnInit() {

    this.lugar = { nombre: '', alias: '', correo: '', telefono: '', descripcion: '', direccion: '' };
  }

  crearLugar(f: NgForm) {

    this.lugar.codigoPostalId = {id:this.registroService.codigoPostal.id};
    this.lugar.segmentoId = {id:1};

    console.log(this.lugar);
    this.adminService.creaLugarPublico(this.lugar).subscribe((dat:CUDResponse)=>{
       this.adminService.getLugar(dat.id).subscribe((lug:Lugar)=>{
        this.adminService.lugar = lug;
        this.router.navigate(['/pages/lugar']);
      });

      // this.lugar.id = 
    });
  }

}
