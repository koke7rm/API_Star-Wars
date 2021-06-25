package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Personaje;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Personaje entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonajeRepository extends JpaRepository<Personaje, Long> {
    List<Personaje> findAllByInvolucrados_nombre(String nombre);
    Optional<Personaje> findByNombre(String nombre);
    List<Personaje> findAllByIntegrantes_nombre(String nombre);
}
