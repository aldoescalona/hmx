import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { AdminService } from 'src/app/services/admin.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {

  constructor(private adminService: AdminService, public router: Router) {

  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Promise<boolean> {


    return new Promise((resolve, reject) => {

      let b = this.adminService.healthCkeck();

      if (b === true) {
        resolve(true);
      } else {
        console.log('Bloqueado por guard');
        this.adminService.logout();
        this.router.navigate(['/login']);
        reject(false);
      }
    });

  }
}
