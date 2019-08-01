import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Credencial } from 'src/app/model/clases';
import { Router } from '@angular/router';
import { ClienteService } from 'src/app/services/cliente.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  ocupado = false;
  recuerdame: boolean = false;
  credencial: Credencial = new Credencial('', '');


  constructor(private cteService: ClienteService,  private router: Router) { }

  ngOnInit() {

    
    this.credencial.username = localStorage.getItem("cte.username") || '';
    if (this.credencial.username.length > 0) {
      this.recuerdame = true;
    }
  }

  login(forma: NgForm) {

    if (forma.invalid) {
      return;
    }

    if (this.ocupado) {
      return;
    }

    this.ocupado = true;

    this.cteService.login(this.credencial, this.recuerdame).subscribe(
      res => {
        this.router.navigate(['/pages']);
        this.ocupado = false;
      },
      (err: HttpErrorResponse ) => {
        if (err.status === 403) {
          swal('Inicio Error', 'Usuario o contraseña incorrectos', 'error');
        }else {
          swal('Inicio Error', 'Problemas de inicio de sesión', 'error');
        }
        console.log(err);
        this.ocupado = false;
      });

  }

}
