import { Component, OnInit } from '@angular/core';
import { Lugar, CUDResponse } from 'src/app/model/interfaces';
import { AdminService } from 'src/app/services/admin.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-lugar-edit',
  templateUrl: './lugar-edit.component.html',
  styles: []
})
export class LugarEditComponent implements OnInit {

  lugar: Lugar = null;

  constructor(public adminService: AdminService, public router: Router) { }

  ngOnInit() {
    this.getLugar();
  }

  getLugar() {
    this.adminService.currentLugar().subscribe((dat: Lugar) => {
      this.lugar = dat;

    });
  }


  guardarLugar() {
    this.adminService.modificarLugar(this.lugar).subscribe((data: CUDResponse) => {
      console.log(data);
      swal('Establecimiento modificado', 'Modificado correctamente', 'success');
      this.router.navigate(['/pages/lugar']);
    });

  }
}
