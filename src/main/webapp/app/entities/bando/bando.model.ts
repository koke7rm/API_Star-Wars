import { IPersonaje } from 'app/entities/personaje/personaje.model';
import { IBatalla } from 'app/entities/batalla/batalla.model';

export interface IBando {
  id?: number;
  nombre?: string | null;
  logoContentType?: string | null;
  logo?: string | null;
  integrantes?: IPersonaje | null;
  ganadors?: IBatalla[] | null;
  perdedors?: IBatalla[] | null;
}

export class Bando implements IBando {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public logoContentType?: string | null,
    public logo?: string | null,
    public integrantes?: IPersonaje | null,
    public ganadors?: IBatalla[] | null,
    public perdedors?: IBatalla[] | null
  ) {}
}

export function getBandoIdentifier(bando: IBando): number | undefined {
  return bando.id;
}
