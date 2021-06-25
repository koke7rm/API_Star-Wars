package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pelicula;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pelicula entity.
 */
@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    @Query(
        value = "select distinct pelicula from Pelicula pelicula left join fetch pelicula.personajes",
        countQuery = "select count(distinct pelicula) from Pelicula pelicula"
    )
    Page<Pelicula> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct pelicula from Pelicula pelicula left join fetch pelicula.personajes")
    List<Pelicula> findAllWithEagerRelationships();

    @Query("select pelicula from Pelicula pelicula left join fetch pelicula.personajes where pelicula.id =:id")
    Optional<Pelicula> findOneWithEagerRelationships(@Param("id") Long id);
}
