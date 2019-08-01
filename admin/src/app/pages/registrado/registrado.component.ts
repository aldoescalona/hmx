import { Component, OnInit } from '@angular/core';
import { FichaRegistroRespuesta, Lugar } from 'src/app/model/interfaces';
import { RegistroService } from 'src/app/services/registro.service';
import { Router } from '@angular/router';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-registrado',
  templateUrl: './registrado.component.html',
  styleUrls: ['./registrado.component.css']
})
export class RegistradoComponent implements OnInit {

  public respuesta:FichaRegistroRespuesta = null;

  constructor(public registroService:RegistroService,
    private adminService:AdminService,
    public router: Router) { }

  ngOnInit() {
    this.respuesta = this.registroService.respuesta;
    this.adminService.getLugar(this.respuesta.lugarId).subscribe((dat:Lugar)=>{
      this.adminService.seleccionaLugar(dat);
    });
  }

  continuar(){
    this.router.navigate(["/pages/lugar"]);
  }
}
