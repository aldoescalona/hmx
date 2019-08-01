import { Injectable, EventEmitter } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http/src/response';
import { tap, map } from 'rxjs/operators'
import { RespuestaSimple, Lugar, CUDResponse, Oferta, Evento, Operador, MapaCliente, FichaRegistro, FichaRegistroRespuesta } from 'src/app/model/interfaces';
import { Credencial, RespuestaSimpleForm } from 'src/app/model/clases';



@Injectable({
  providedIn: 'root'
})
export class AdminService {

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

    let url = `${environment.API_URL}/auth/admin/login`;

    return this.http.post(url, idOp).pipe(
      tap(res => {
        this.token = res['token'];
        localStorage.setItem('ad.token', this.token);
        if (recuerdame) {
          localStorage.setItem('adm.username', idOp.username);
        } else {
          localStorage.removeItem('adm.username');
        }
      }, (err: HttpErrorResponse ) => {
        this.token = null;
        if (err.status === 403) {
          console.log("Remover TOKEN")
          localStorage.removeItem('ad.token');
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('ad.token');
    this.token = '';
    this.lugar = null;
  }

  renovarToken() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/auth/admin/renovar`;
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
      localStorage.removeItem('ad.token');
      return false;
    }

    ahora.setTime(ahora.getTime() + (this.HORAS * 60 * 60 * 1000));

    if (tokenExp.getTime() < ahora.getTime()) {
      this.renovarToken().subscribe(res => {
        this.token = res['token'];
        localStorage.setItem('ad.token', this.token);
      });
    }
    return true;
  }

  cargaStorage() {
    this.token = localStorage.getItem('ad.token');
  }

  adminFirmado() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/admin/id`;
    return this.http.get(url, { headers: reqHeader });
  }

  registro(ficha: FichaRegistro) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/admin/registro`;
    return this.http.post<FichaRegistroRespuesta>(url, ficha, { headers: reqHeader });
  }

  lugares() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/admin/lugares`;
    return this.http.get(url, { headers: reqHeader });
  }

  lugaresPublico() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/admin/lugares/publico`;
    return this.http.get(url, { headers: reqHeader });
  }

  getLugar(id:number) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/admin/lugar/${id}`;
    return this.http.get(url, { headers: reqHeader });
  }


  currentLugar() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/admin/lugar/${this.lugar.id}`;
    return this.http.get(url, { headers: reqHeader });
  }

  seleccionaLugar(lugar: Lugar) {
    this.lugar = lugar;
    this.menucheck();
    this.notificacion.emit(this.menu);
  }

  creaLugar(lugar: Lugar) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/admin/lugar`;
    return this.http.post<CUDResponse>(url, lugar, { headers: reqHeader });
  }

  creaLugarPublico(lugar: Lugar) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/admin/lugar/no_admin`;
    return this.http.post<CUDResponse>(url, lugar, { headers: reqHeader });
  }

  modificarLugar(lugar: Lugar) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/admin/lugar/${lugar.id}`;
    return this.http.put<CUDResponse>(url, lugar, { headers: reqHeader });
  }

  bajaOferta(id: number) {

    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/admin/oferta/${id}`;
    return this.http.delete<CUDResponse>(url, { headers: reqHeader });
  }


  eventos() {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/admin/eventos/${this.lugar.id}`;
    return this.http.get(url, { headers: reqHeader });
  }

  evento(id: string) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/admin/evento/${id}`;
    return this.http.get(url, { headers: reqHeader });
  }

  guardarEvento(evento: Evento) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/admin/evento`;
    return this.http.post<CUDResponse>(url, evento, { headers: reqHeader });
  }

  modificaEvento(evento: Evento, id: number) {

    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/admin/evento/${id}`;
    return this.http.put<CUDResponse>(url, evento, { headers: reqHeader });
  }

  bajaEvento(id: number) {

    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/admin/evento/${id}`;
    return this.http.delete<CUDResponse>(url, { headers: reqHeader });
  }



  cps(cp:string) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    })

    let url = `${environment.API_URL}/codigoPostal/${cp}`;
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

  
  cambiaPassCliente(operador: Operador) {
    var reqHeader = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': this.token
    });

    let url = `${environment.API_URL}/admin/cliente/cambiarpass/${operador.id}`;
    return this.http.put<CUDResponse>(url, operador, { headers: reqHeader });
  }

  menucheck() {

    if (this.lugar != null) {
      this.menu = [
        {
          nombre: "Matenimiento",
  
          hijos: [
            { nombre: "Eventos", icono: 'fa-calendar-day', url: "/pages/eventos" }
          ]
        }];
    } else {
      this.menu = [{
        nombre: "Plataforma",
        hijos: [
          { nombre: "Inicio", icono: 'fa-home', url: "/pages/dashboard" },
          { nombre: "Nuevo registro", icono: 'fa-plus', url: "/pages/localiza/adm" },
          { nombre: "Establecimientos", icono: 'fa-store', url: "/pages/lugares/adm" },
          { nombre: "Dominio publico", icono: 'fa-archway', url: "/pages/lugares/noadm" }
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
