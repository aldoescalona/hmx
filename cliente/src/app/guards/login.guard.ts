import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { ClienteService } from 'src/app/services/cliente.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {

  constructor(private clienteService: ClienteService, public router: Router) {

  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Promise<boolean> {


    return new Promise((resolve, reject) => {

      let b = this.clienteService.healthCkeck();

      if (b === true) {
        resolve(true);
      } else {
        console.log('Bloqueado por guard');
        this.clienteService.logout();
        this.router.navigate(['/login']);
        reject(false);
      }
    });

  }
}
