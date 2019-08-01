import { Component, OnInit } from '@angular/core';
import { CredencialOp } from 'src/app/model/CredencialOp';
import { OperadorService } from 'src/app/services/operador.service';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http/src/response';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  ocupado = false;
  recuerdame: boolean = false;
  credencial: CredencialOp = new CredencialOp('', '', '');

  constructor(private opService: OperadorService,
    private router: Router) { }

  ngOnInit() {
    this.credencial.alias = localStorage.getItem("op.alias") || '';
    this.credencial.username = localStorage.getItem("op.username") || '';
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

    this.opService.login(this.credencial, this.recuerdame).subscribe(
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
