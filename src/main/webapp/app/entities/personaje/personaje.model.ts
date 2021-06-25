import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { IBando } from 'app/entities/bando/bando.model';
import { IBatalla } from 'app/entities/batalla/batalla.model';

export interface IPersonaje {
  id?: number;
  nombre?: string | null;
  edad?: number | null;
  rango?: string | null;
  especie?: string | null;
  personajes?: IPelicula[] | null;
  integrantes?: IBando[] | null;
  involucrados?: IBatalla[] | null;
}

export class Personaje implements IPersonaje {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public edad?: number | null,
    public rango?: string | null,
    public especie?: string | null,
    public personajes?: IPelicula[] | null,
    public integrantes?: IBando[] | null,
    public involucrados?: IBatalla[] | null
  ) {}
}

export function getPersonajeIdentifier(personaje: IPersonaje): number | undefined {
  return personaje.id;
}
