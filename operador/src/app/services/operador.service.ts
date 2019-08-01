import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CredencialOp, RespuestaSimpleForm, Busqueda } from 'src/app/model/CredencialOp';
import { environment } from 'src/environments/environment';

import { tap, map } from 'rxjs/operators'
import { RespuestaSimple, Lugar, DashboardOperador } from 'src/app/model/interfaces';
import { HttpErrorResponse } from '@angular/common/http/src/response';

@Injectable({
  providedIn: 'root'
})
export class OperadorService {

  public lugar: Lugar = null;
  private HORAS: number = 2;
  private token: string;

  constructor(private http: HttpClient) {

    this.cargaStorage();
  }


  login(idOp: CredencialOp, recuerdame: boolean) {

    let url = `${environment.API_URL}/auth/operador/login`;

    return this.http.post(url, idOp).pipe(
      tap(res => {
        this.token = res['token'];
        localStorage.setItem('op.token', this.token);
        if (recuerdame) {
          localStorage.setItem('op.alias', idOp.alias);
          localStorage.setItem('op.username', idOp.username);
        } else {
          localStorage.removeItem('op.alias');
          localStorage.removeItem('op.username');
        }
      }, (err: HttpErrorResponse ) => {
        this.token = null;
        if (err.status === 403) {
          console.log("Remover TOKEN")
          localStorage.removeItem('op.token');
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('op.token');
    this.token = '';
  }

  renovarToken() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/auth/operador/renovar`;
    return this.http.post(url, '', { headers: reqHeader });
  }

  healthCkeck() {

    if (this.token === null || this.token.length < 5) {
      return false;
    }

    let payload = JSON.parse(atob(this.token.split('.')[1]));

    let tokenExp = new Date(payload.exp * 1000);

    let ahora = new Date();

    if (tokenExp.getTime() < ahora.getTime()) {
      localStorage.removeItem('op.token');
      return false;
    }

    ahora.setTime(ahora.getTime() + (this.HORAS * 60 *  60 * 1000));
    
    if (tokenExp.getTime() < ahora.getTime()) {
      this.renovarToken().subscribe(res => {
        this.token = res['token'];
        localStorage.setItem('op.token', this.token);
      });
    }
    return true;
  }

  cargaStorage() {
    this.token = localStorage.getItem('op.token');
  }

  dashboard() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/operador/dashboard`;
    return this.http.get(url, { headers: reqHeader }).pipe(
      tap((dat: DashboardOperador) => {
        this.lugar = dat.lugar;
      })
    );
  }

  visitasPendientes() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/operador/visitas/pendientes`;
    return this.http.get(url, { headers: reqHeader });
  }


  buscar(bus: Busqueda) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/operador/buscar`;
    return this.http.post(url, bus, { headers: reqHeader });
  }

  aceptarVisita(respuesta: RespuestaSimpleForm) {

    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/operador/visita/acepta`;
    return this.http.put<RespuestaSimple>(url, respuesta, { headers: reqHeader });
  }

  rechazaVisita(respuesta: RespuestaSimpleForm) {

    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/operador/visita/rechaza`;
    return this.http.put<RespuestaSimple>(url, respuesta, { headers: reqHeader });
  }

  aceptarCompra(respuesta: RespuestaSimpleForm) {

    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/operador/compra/acepta`;
    return this.http.put<RespuestaSimple>(url, respuesta, { headers: reqHeader });
  }

  rechazaCompra(respuesta: RespuestaSimpleForm) {

    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/operador/compra/rechaza`;
    return this.http.put<RespuestaSimple>(url, respuesta, { headers: reqHeader });
  }

}
