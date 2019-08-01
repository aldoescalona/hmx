import { Component, OnInit } from '@angular/core';
import { OperadorService } from 'src/app/services/operador.service';

@Component({
  selector: 'app-pages',
  templateUrl: './pages.component.html',
  styleUrls: ['./pages.component.css']
})
export class PagesComponent implements OnInit {

  constructor(public operadorService:OperadorService) { }

  ngOnInit() {
  }

}
