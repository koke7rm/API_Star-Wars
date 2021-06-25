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
 * A Batalla.
 */
@Entity
@Table(name = "batalla")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Batalla implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", unique = true)
    private String nombre;

    @Column(name = "planeta")
    private String planeta;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_batalla__involucrados",
        joinColumns = @JoinColumn(name = "batalla_id"),
        inverseJoinColumns = @JoinColumn(name = "involucrados_id")
    )
    @JsonIgnoreProperties(value = { "personajes", "integrantes", "involucrados" }, allowSetters = true)
    private Set<Personaje> involucrados = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "personajes", "batallas" }, allowSetters = true)
    private Pelicula pelicula;

    @ManyToOne
    @JsonIgnoreProperties(value = { "integrantes", "ganadors", "perdedors" }, allowSetters = true)
    private Bando ganador;

    @ManyToOne
    @JsonIgnoreProperties(value = { "integrantes", "ganadors", "perdedors" }, allowSetters = true)
    private Bando perdedor;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Batalla id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Batalla nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPlaneta() {
        return this.planeta;
    }

    public Batalla planeta(String planeta) {
        this.planeta = planeta;
        return this;
    }

    public void setPlaneta(String planeta) {
        this.planeta = planeta;
    }

    public Set<Personaje> getInvolucrados() {
        return this.involucrados;
    }

    public Batalla involucrados(Set<Personaje> personajes) {
        this.setInvolucrados(personajes);
        return this;
    }

    public Batalla addInvolucrados(Personaje personaje) {
        this.involucrados.add(personaje);
        personaje.getInvolucrados().add(this);
        return this;
    }

    public Batalla removeInvolucrados(Personaje personaje) {
        this.involucrados.remove(personaje);
        personaje.getInvolucrados().remove(this);
        return this;
    }

    public void setInvolucrados(Set<Personaje> personajes) {
        this.involucrados = personajes;
    }

    public Pelicula getPelicula() {
        return this.pelicula;
    }

    public Batalla pelicula(Pelicula pelicula) {
        this.setPelicula(pelicula);
        return this;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Bando getGanador() {
        return this.ganador;
    }

    public Batalla ganador(Bando bando) {
        this.setGanador(bando);
        return this;
    }

    public void setGanador(Bando bando) {
        this.ganador = bando;
    }

    public Bando getPerdedor() {
        return this.perdedor;
    }

    public Batalla perdedor(Bando bando) {
        this.setPerdedor(bando);
        return this;
    }

    public void setPerdedor(Bando bando) {
        this.perdedor = bando;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Batalla)) {
            return false;
        }
        return id != null && id.equals(((Batalla) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Batalla{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", planeta='" + getPlaneta() + "'" +
            "}";
    }
}
