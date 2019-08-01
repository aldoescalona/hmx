import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
import { Lugar } from 'src/app/model/interfaces';
import { Router } from '@angular/router';
import { ImagenModalService } from 'src/app/comp/imagen-modal/imagen-modal.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  lugares: Lugar[] = [];
  cargando: boolean = false;

  constructor(
    private adminService: AdminService,
    public router: Router,
    public imageModalService: ImagenModalService) { }

  ngOnInit() {

  }

  

}
