import { Component, OnInit } from '@angular/core';
import { ImagenModalService } from 'src/app/comp/imagen-modal/imagen-modal.service';
import { SubirArchivoService } from 'src/app/services/subir-archivo.service';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-imagen-modal',
  templateUrl: './imagen-modal.component.html',
  styleUrls: ['./imagen-modal.component.css']
})
export class ImagenModalComponent implements OnInit {

  imagenSubir: File;
  imagenTemp: any;

  constructor(public imagenModalService:ImagenModalService,
    public _subirArchivoService:AdminService) { }

  ngOnInit() {
  }

  cerrarModal() {
    this.imagenTemp = null;
    this.imagenSubir = null;

    this.imagenModalService.ocultarModal();
  }

  seleccionImage( archivo: File ) {

    if ( !archivo ) {
      this.imagenSubir = null;
      return;
    }

    if ( archivo.type.indexOf('image') < 0 ) {
      swal('Sólo imágenes', 'El archivo seleccionado no es una imagen', 'error');
      this.imagenSubir = null;
      return;
    }

    this.imagenSubir = archivo;

    let reader = new FileReader();
    let urlImagenTemp = reader.readAsDataURL( archivo );

    reader.onloadend = () => {
      // console.log(reader.result);
      this.imagenTemp = reader['result'];
    };

  }

  subirImagen() {

    console.log('SUBIR IMAGEN');
    this._subirArchivoService.subirArchivo( this.imagenSubir, this.imagenModalService.tipo, this.imagenModalService.id )
    .then( resp => {
      console.log('IMAGEN SUBIDA');
      this.imagenModalService.notificacion.emit( resp );
      this.cerrarModal();

    })
    .catch( err => {
      console.log( err);
    });

  }

}
