package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Bando;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Bando entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BandoRepository extends JpaRepository<Bando, Long> {}
