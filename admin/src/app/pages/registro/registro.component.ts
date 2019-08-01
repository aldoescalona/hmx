import { Component, OnInit } from '@angular/core';
import { Lugar, Cliente, Operador, FichaRegistro, FichaRegistroRespuesta } from 'src/app/model/interfaces';
import { NgForm } from '@angular/forms';
import { AdminService } from 'src/app/services/admin.service';
import { Router } from '@angular/router';
import { RegistroService } from 'src/app/services/registro.service';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent implements OnInit {

  

  ficha: FichaRegistro = {
    cliente: { nombre: '', correo: '' },
    lugar: { nombre: '', alias: '', correo: '', telefono: '', descripcion: '', direccion: '' },
    operador: { nombre: '', clave: '' }
  };

  constructor(private adminService: AdminService, 
    private registroService:RegistroService,
    public router: Router) {
   
  }

  ngOnInit() {
  }

  registrarFicha(f: NgForm) {

    this.ficha.lugar.codigoPostalId = {id:this.registroService.codigoPostal.id};
    this.ficha.lugar.segmentoId = {id:1};

    this.adminService.registro(this.ficha).subscribe((dat:FichaRegistroRespuesta)=>{
      this.registroService.ficha = this.ficha;
      this.registroService.respuesta = dat;
      this.router.navigate(['/pages/registrado']);
    });
  }

}
