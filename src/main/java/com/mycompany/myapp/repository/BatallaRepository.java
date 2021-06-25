package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Batalla;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Batalla entity.
 */
@Repository
public interface BatallaRepository extends JpaRepository<Batalla, Long> {
    @Query(
        value = "select distinct batalla from Batalla batalla left join fetch batalla.involucrados",
        countQuery = "select count(distinct batalla) from Batalla batalla"
    )
    Page<Batalla> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct batalla from Batalla batalla left join fetch batalla.involucrados")
    List<Batalla> findAllWithEagerRelationships();

    @Query("select batalla from Batalla batalla left join fetch batalla.involucrados where batalla.id =:id")
    Optional<Batalla> findOneWithEagerRelationships(@Param("id") Long id);

    List<Batalla> findByGanador_nombreAndPlaneta(String ganador, String planeta);
    List<Batalla> findByInvolucrados_nombre(String nombre);
}
