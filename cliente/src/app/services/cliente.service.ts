import { Injectable, EventEmitter } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http/src/response';
import { tap, map } from 'rxjs/operators'
import { RespuestaSimple, Lugar, CUDResponse, Oferta, Evento, Operador, MapaCliente } from 'src/app/model/interfaces';
import { Credencial, RespuestaSimpleForm } from 'src/app/model/clases';



@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  public lugar: Lugar = null;
  private HORAS: number = 2;
  private token: string;
  private mapaCliente: MapaCliente;

  public notificacion = new EventEmitter<any>();

  menu: any = [];

  constructor(private http: HttpClient) {
    this.cargaStorage();
    this.menucheck();
  }

  login(idOp: Credencial, recuerdame: boolean) {

    let url = `${environment.API_URL}/auth/cliente/login`;

    return this.http.post(url, idOp).pipe(
      tap(res => {
        this.token = res['token'];
        localStorage.setItem('ct.token', this.token);
        if (recuerdame) {
          localStorage.setItem('cte.username', idOp.username);
        } else {
          localStorage.removeItem('cte.username');
        }
      }, (err: HttpErrorResponse ) => {
        this.token = null;
        if (err.status === 403) {
          console.log("Remover TOKEN")
          localStorage.removeItem('ct.token');
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('ct.token');
    this.token = '';
    this.lugar = null;
  }

  renovarToken() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/auth/cliente/renovar`;
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
      localStorage.removeItem('ct.token');
      return false;
    }

    ahora.setTime(ahora.getTime() + (this.HORAS * 60 * 60 * 1000));

    if (tokenExp.getTime() < ahora.getTime()) {
      this.renovarToken().subscribe(res => {
        this.token = res['token'];
        localStorage.setItem('ct.token', this.token);
      });
    }
    return true;
  }

  cargaStorage() {
    this.token = localStorage.getItem('ct.token');
  }

  clienteFirmado() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/cliente/id`;
    return this.http.get(url, { headers: reqHeader });
  }

  lugares() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/cliente/lugares`;
    return this.http.get(url, { headers: reqHeader });
  }


  currentLugar() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/cliente/lugar/${this.lugar.id}`;
    return this.http.get(url, { headers: reqHeader });
  }

  seleccionaLugar(lugar: Lugar) {
    this.lugar = lugar;
    this.menucheck();
    this.notificacion.emit(this.menu);
  }

  modificarLugar(lugar: Lugar) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/cliente/lugar/${lugar.id}`;
    return this.http.put<CUDResponse>(url, lugar, { headers: reqHeader });
  }

  ofertas() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/cliente/ofertas/${this.lugar.id}`;
    return this.http.get(url, { headers: reqHeader });
  }

  oferta(id: string) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/cliente/oferta/${id}`;
    return this.http.get(url, { headers: reqHeader });
  }

  guardarOferta(oferta: Oferta) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/cliente/oferta`;
    return this.http.post<CUDResponse>(url, oferta, { headers: reqHeader });
  }

  modificaOferta(oferta: Oferta, id: number) {

    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/cliente/oferta/${id}`;
    return this.http.put<CUDResponse>(url, oferta, { headers: reqHeader });
  }

  bajaOferta(id: number) {

    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/cliente/oferta/${id}`;
    return this.http.delete<CUDResponse>(url, { headers: reqHeader });
  }


  eventos() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/cliente/eventos/${this.lugar.id}`;
    return this.http.get(url, { headers: reqHeader });
  }

  evento(id: string) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/cliente/evento/${id}`;
    return this.http.get(url, { headers: reqHeader });
  }

  guardarEvento(evento: Evento) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/cliente/evento`;
    return this.http.post<CUDResponse>(url, evento, { headers: reqHeader });
  }

  modificaEvento(evento: Evento, id: number) {

    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/cliente/evento/${id}`;
    return this.http.put<CUDResponse>(url, evento, { headers: reqHeader });
  }

  bajaEvento(id: number) {

    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/cliente/evento/${id}`;
    return this.http.delete<CUDResponse>(url, { headers: reqHeader });
  }


  operadores() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/cliente/operadores/${this.lugar.id}`;
    return this.http.get(url, { headers: reqHeader });
  }

  operador(id: string) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/cliente/operador/${id}`;
    return this.http.get(url, { headers: reqHeader });
  }

  guardarOperador(operador: Operador) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/cliente/operador`;
    return this.http.post<CUDResponse>(url, operador, { headers: reqHeader });
  }

  modificaOperador(operador: Operador, id: number) {

    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/cliente/operador/${id}`;
    return this.http.put<CUDResponse>(url, operador, { headers: reqHeader });
  }

  bajaOperador(id: number) {

    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/cliente/operador/${id}`;
    return this.http.delete<CUDResponse>(url, { headers: reqHeader });
  }


  cambiaPassOperador(operador: Operador) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/cliente/operador/cambiarpass/${operador.id}`;
    return this.http.put<CUDResponse>(url, operador, { headers: reqHeader });
  }

  menucheck() {

    if (this.lugar != null) {
      this.menu = [
        {
          nombre: "Matenimiento",
  
          hijos: [
            { nombre: "Ofertas", icono: 'fa-tag', url: "/pages/ofertas" },
            { nombre: "Eventos", icono: 'fa-calendar-day', url: "/pages/eventos" },
            { nombre: "Operadores", icono: 'fa-user', url: "/pages/operadores" }
          ]
        }/*, {
        nombre: "Análisis",
        hijos: [{
          nombre: "Reportes", link: "abc", icono: 'fa-chart-pie', titulo: "Hijos",
          nietos: [
            { nombre: "Visitas", link: "asdd", url: "/pages/dashboard" },
            { nombre: "Ofertas", link: "asdd", url: "/pages/dashboard" }
          ]
        },
        { nombre: "Puntos", link: "abc 2", icono: 'fa-coins', url: "/pages/dashboard" }
        ]
      }*/];
    } else {
      this.menu = [{
        nombre: "Plataforma",
        hijos: [
          { nombre: "Inicio", icono: 'fa-home', url: "/pages/dashboard" },
          { nombre: "Pagos", icono: 'fa-dollar-sign', url: "/pages/pagos" },
          { nombre: "Análisis", icono: 'fa-chart-pie', url: "/pages/reportes" }
        ]
      }];
    }


  }

  subirArchivo(archivo: File, tipo: string, id: number) {

    return new Promise((resolve, reject) => {

      let formData = new FormData();
      let xhr = new XMLHttpRequest();

      formData.append('file', archivo, archivo.name);

      xhr.onreadystatechange = function () {

        if (xhr.readyState === 4) {

          if (xhr.status === 200) {
            console.log('Imagen subida');
            resolve(JSON.parse(xhr.response));
          } else {
            console.log('Fallo la subida', xhr.status, xhr.response);
            reject(xhr.response);
          }

        }
      };

      // let url = 'http://localhost:8080/api/v2/imagen/upload/evento/2';
      let url = `${environment.API_URL}/imagen/upload/${tipo}/${id}`;

      xhr.open('POST', url, true);
      xhr.setRequestHeader("Authorization", this.token);
      xhr.send(formData);

    });

  }


}
