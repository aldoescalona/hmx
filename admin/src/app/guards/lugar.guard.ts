import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { AdminService } from 'src/app/services/admin.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class LugarGuard implements CanActivate {

constructor(private adminService:AdminService, public router: Router){

}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    

      return new Promise((resolve, reject) => {

        let l = this.adminService.lugar
  
        if (l) {
          resolve(true);
        } else {
          this.router.navigate(['/pages/dashboard']);
          reject(false);
        }
      });
  
  }
}
