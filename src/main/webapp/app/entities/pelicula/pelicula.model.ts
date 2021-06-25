import * as dayjs from 'dayjs';
import { IPersonaje } from 'app/entities/personaje/personaje.model';
import { IBatalla } from 'app/entities/batalla/batalla.model';

export interface IPelicula {
  id?: number;
  titulo?: string | null;
  episodio?: number | null;
  estreno?: dayjs.Dayjs | null;
  personajes?: IPersonaje[] | null;
  batallas?: IBatalla[] | null;
}

export class Pelicula implements IPelicula {
  constructor(
    public id?: number,
    public titulo?: string | null,
    public episodio?: number | null,
    public estreno?: dayjs.Dayjs | null,
    public personajes?: IPersonaje[] | null,
    public batallas?: IBatalla[] | null
  ) {}
}

export function getPeliculaIdentifier(pelicula: IPelicula): number | undefined {
  return pelicula.id;
}
