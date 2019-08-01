import { Pipe, PipeTransform } from '@angular/core';
import { environment } from 'src/environments/environment';

@Pipe({
  name: 'imagen'
})
export class ImagenPipe implements PipeTransform {

  transform(value: string): string {
    let url =`${environment.IMG_URL}/${value}`; 
    return url;
  }

}
