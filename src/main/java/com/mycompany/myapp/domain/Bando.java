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
 * A Bando.
 */
@Entity
@Table(name = "bando")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bando implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", unique = true)
    private String nombre;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "personajes", "integrantes", "involucrados" }, allowSetters = true)
    private Personaje integrantes;

    @OneToMany(mappedBy = "ganador")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "involucrados", "pelicula", "ganador", "perdedor" }, allowSetters = true)
    private Set<Batalla> ganadors = new HashSet<>();

    @OneToMany(mappedBy = "perdedor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "involucrados", "pelicula", "ganador", "perdedor" }, allowSetters = true)
    private Set<Batalla> perdedors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bando id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Bando nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Bando logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Bando logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Personaje getIntegrantes() {
        return this.integrantes;
    }

    public Bando integrantes(Personaje personaje) {
        this.setIntegrantes(personaje);
        return this;
    }

    public void setIntegrantes(Personaje personaje) {
        this.integrantes = personaje;
    }

    public Set<Batalla> getGanadors() {
        return this.ganadors;
    }

    public Bando ganadors(Set<Batalla> batallas) {
        this.setGanadors(batallas);
        return this;
    }

    public Bando addGanador(Batalla batalla) {
        this.ganadors.add(batalla);
        batalla.setGanador(this);
        return this;
    }

    public Bando removeGanador(Batalla batalla) {
        this.ganadors.remove(batalla);
        batalla.setGanador(null);
        return this;
    }

    public void setGanadors(Set<Batalla> batallas) {
        if (this.ganadors != null) {
            this.ganadors.forEach(i -> i.setGanador(null));
        }
        if (batallas != null) {
            batallas.forEach(i -> i.setGanador(this));
        }
        this.ganadors = batallas;
    }

    public Set<Batalla> getPerdedors() {
        return this.perdedors;
    }

    public Bando perdedors(Set<Batalla> batallas) {
        this.setPerdedors(batallas);
        return this;
    }

    public Bando addPerdedor(Batalla batalla) {
        this.perdedors.add(batalla);
        batalla.setPerdedor(this);
        return this;
    }

    public Bando removePerdedor(Batalla batalla) {
        this.perdedors.remove(batalla);
        batalla.setPerdedor(null);
        return this;
    }

    public void setPerdedors(Set<Batalla> batallas) {
        if (this.perdedors != null) {
            this.perdedors.forEach(i -> i.setPerdedor(null));
        }
        if (batallas != null) {
            batallas.forEach(i -> i.setPerdedor(this));
        }
        this.perdedors = batallas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bando)) {
            return false;
        }
        return id != null && id.equals(((Bando) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bando{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            "}";
    }
}
