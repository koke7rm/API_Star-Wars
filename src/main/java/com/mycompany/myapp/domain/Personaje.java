package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Personaje.
 */
@Entity
@Table(name = "personaje")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Personaje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", unique = true)
    private String nombre;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "rango")
    private String rango;

    @Column(name = "especie")
    private String especie;

    @ManyToMany(mappedBy = "personajes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "personajes", "batallas" }, allowSetters = true)
    private Set<Pelicula> personajes = new HashSet<>();

    @OneToMany(mappedBy = "integrantes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "integrantes", "ganadors", "perdedors" }, allowSetters = true)
    private Set<Bando> integrantes = new HashSet<>();

    @ManyToMany(mappedBy = "involucrados")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "involucrados", "pelicula", "ganador", "perdedor" }, allowSetters = true)
    private Set<Batalla> involucrados = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Personaje id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Personaje nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEdad() {
        return this.edad;
    }

    public Personaje edad(Integer edad) {
        this.edad = edad;
        return this;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getRango() {
        return this.rango;
    }

    public Personaje rango(String rango) {
        this.rango = rango;
        return this;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getEspecie() {
        return this.especie;
    }

    public Personaje especie(String especie) {
        this.especie = especie;
        return this;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public Set<Pelicula> getPersonajes() {
        return this.personajes;
    }

    public Personaje personajes(Set<Pelicula> peliculas) {
        this.setPersonajes(peliculas);
        return this;
    }

    public Personaje addPersonajes(Pelicula pelicula) {
        this.personajes.add(pelicula);
        pelicula.getPersonajes().add(this);
        return this;
    }

    public Personaje removePersonajes(Pelicula pelicula) {
        this.personajes.remove(pelicula);
        pelicula.getPersonajes().remove(this);
        return this;
    }

    public void setPersonajes(Set<Pelicula> peliculas) {
        if (this.personajes != null) {
            this.personajes.forEach(i -> i.removePersonajes(this));
        }
        if (peliculas != null) {
            peliculas.forEach(i -> i.addPersonajes(this));
        }
        this.personajes = peliculas;
    }

    public Set<Bando> getIntegrantes() {
        return this.integrantes;
    }

    public Personaje integrantes(Set<Bando> bandos) {
        this.setIntegrantes(bandos);
        return this;
    }

    public Personaje addIntegrantes(Bando bando) {
        this.integrantes.add(bando);
        bando.setIntegrantes(this);
        return this;
    }

    public Personaje removeIntegrantes(Bando bando) {
        this.integrantes.remove(bando);
        bando.setIntegrantes(null);
        return this;
    }

    public void setIntegrantes(Set<Bando> bandos) {
        if (this.integrantes != null) {
            this.integrantes.forEach(i -> i.setIntegrantes(null));
        }
        if (bandos != null) {
            bandos.forEach(i -> i.setIntegrantes(this));
        }
        this.integrantes = bandos;
    }

    public Set<Batalla> getInvolucrados() {
        return this.involucrados;
    }

    public Personaje involucrados(Set<Batalla> batallas) {
        this.setInvolucrados(batallas);
        return this;
    }

    public Personaje addInvolucrados(Batalla batalla) {
        this.involucrados.add(batalla);
        batalla.getInvolucrados().add(this);
        return this;
    }

    public Personaje removeInvolucrados(Batalla batalla) {
        this.involucrados.remove(batalla);
        batalla.getInvolucrados().remove(this);
        return this;
    }

    public void setInvolucrados(Set<Batalla> batallas) {
        if (this.involucrados != null) {
            this.involucrados.forEach(i -> i.removeInvolucrados(this));
        }
        if (batallas != null) {
            batallas.forEach(i -> i.addInvolucrados(this));
        }
        this.involucrados = batallas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personaje)) {
            return false;
        }
        return id != null && id.equals(((Personaje) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personaje{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", edad=" + getEdad() +
            ", rango='" + getRango() + "'" +
            ", especie='" + getEspecie() + "'" +
            "}";
    }
}
