import { IPersonaje } from 'app/entities/personaje/personaje.model';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { IBando } from 'app/entities/bando/bando.model';

export interface IBatalla {
  id?: number;
  nombre?: string | null;
  planeta?: string | null;
  involucrados?: IPersonaje[] | null;
  pelicula?: IPelicula | null;
  ganador?: IBando | null;
  perdedor?: IBando | null;
}

export class Batalla implements IBatalla {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public planeta?: string | null,
    public involucrados?: IPersonaje[] | null,
    public pelicula?: IPelicula | null,
    public ganador?: IBando | null,
    public perdedor?: IBando | null
  ) {}
}

export function getBatallaIdentifier(batalla: IBatalla): number | undefined {
  return batalla.id;
}
