import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { OperadorService } from 'src/app/services/operador.service';

@Component({
  selector: 'app-menubar',
  templateUrl: './menubar.component.html',
  styleUrls: ['./menubar.component.css']
})
export class MenubarComponent implements OnInit {

  termino:string;
  constructor(private opService: OperadorService, 
    private router:Router) { }

  ngOnInit() {
  }

  buscar( forma: NgForm) {
    
    console.log('Buscar', this.termino);
    this.router.navigate(['/pages/busqueda', {termino: this.termino}]);
  }


  salir(){
    this.opService.logout();
    this.router.navigate(['/login']);
  }
}
