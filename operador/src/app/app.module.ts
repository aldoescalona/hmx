import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { PagesModule } from 'src/app/pages/pages.module';
import { OperadorService } from 'src/app/services/operador.service';
import { LoginGuard } from 'src/app/guards/login.guard';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    PagesModule,
    HttpClientModule
  ],
  providers: [OperadorService, LoginGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
