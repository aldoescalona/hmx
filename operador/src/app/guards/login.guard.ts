import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OperadorService } from 'src/app/services/operador.service';
import { CanActivateChild, Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';



@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {

  constructor(private opeardorService: OperadorService, public router: Router) {

  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Promise<boolean> {

    console.log('GUARD ')
    
    
    return new Promise( (resolve, reject) => {

      let b = this.opeardorService.healthCkeck();

      if (b === true) {
        resolve(true);
      } else {
        console.log('Bloqueado por guard');
        this.router.navigate(['/login']);
        reject(false);
      }
    });
    
  }

}
