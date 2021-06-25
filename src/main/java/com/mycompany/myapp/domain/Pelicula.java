package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pelicula.
 */
@Entity
@Table(name = "pelicula")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pelicula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", unique = true)
    private String titulo;

    @Column(name = "episodio")
    private Integer episodio;

    @Column(name = "estreno")
    private LocalDate estreno;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_pelicula__personajes",
        joinColumns = @JoinColumn(name = "pelicula_id"),
        inverseJoinColumns = @JoinColumn(name = "personajes_id")
    )
    @JsonIgnoreProperties(value = { "personajes", "integrantes", "involucrados" }, allowSetters = true)
    private Set<Personaje> personajes = new HashSet<>();

    @OneToMany(mappedBy = "pelicula")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "involucrados", "pelicula", "ganador", "perdedor" }, allowSetters = true)
    private Set<Batalla> batallas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pelicula id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Pelicula titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getEpisodio() {
        return this.episodio;
    }

    public Pelicula episodio(Integer episodio) {
        this.episodio = episodio;
        return this;
    }

    public void setEpisodio(Integer episodio) {
        this.episodio = episodio;
    }

    public LocalDate getEstreno() {
        return this.estreno;
    }

    public Pelicula estreno(LocalDate estreno) {
        this.estreno = estreno;
        return this;
    }

    public void setEstreno(LocalDate estreno) {
        this.estreno = estreno;
    }

    public Set<Personaje> getPersonajes() {
        return this.personajes;
    }

    public Pelicula personajes(Set<Personaje> personajes) {
        this.setPersonajes(personajes);
        return this;
    }

    public Pelicula addPersonajes(Personaje personaje) {
        this.personajes.add(personaje);
        personaje.getPersonajes().add(this);
        return this;
    }

    public Pelicula removePersonajes(Personaje personaje) {
        this.personajes.remove(personaje);
        personaje.getPersonajes().remove(this);
        return this;
    }

    public void setPersonajes(Set<Personaje> personajes) {
        this.personajes = personajes;
    }

    public Set<Batalla> getBatallas() {
        return this.batallas;
    }

    public Pelicula batallas(Set<Batalla> batallas) {
        this.setBatallas(batallas);
        return this;
    }

    public Pelicula addBatalla(Batalla batalla) {
        this.batallas.add(batalla);
        batalla.setPelicula(this);
        return this;
    }

    public Pelicula removeBatalla(Batalla batalla) {
        this.batallas.remove(batalla);
        batalla.setPelicula(null);
        return this;
    }

    public void setBatallas(Set<Batalla> batallas) {
        if (this.batallas != null) {
            this.batallas.forEach(i -> i.setPelicula(null));
        }
        if (batallas != null) {
            batallas.forEach(i -> i.setPelicula(this));
        }
        this.batallas = batallas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pelicula)) {
            return false;
        }
        return id != null && id.equals(((Pelicula) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pelicula{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", episodio=" + getEpisodio() +
            ", estreno='" + getEstreno() + "'" +
            "}";
    }
}
