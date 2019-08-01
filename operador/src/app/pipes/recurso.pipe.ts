import { Pipe, PipeTransform } from '@angular/core';
import { environment } from 'src/environments/environment';

@Pipe({
  name: 'recurso'
})
export class RecursoPipe implements PipeTransform {

  transform(value: string): string {
    let url =`${environment.IMGRSC_URL}/${value}`;
    return url;
  }

}
