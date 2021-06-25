package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Batalla;
import com.mycompany.myapp.repository.BatallaRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Batalla}.
 */
@Service
@Transactional
public class BatallaService {

    private final Logger log = LoggerFactory.getLogger(BatallaService.class);

    private final BatallaRepository batallaRepository;

    public BatallaService(BatallaRepository batallaRepository) {
        this.batallaRepository = batallaRepository;
    }

    /**
     * Save a batalla.
     *
     * @param batalla the entity to save.
     * @return the persisted entity.
     */
    public Batalla save(Batalla batalla) {
        log.debug("Request to save Batalla : {}", batalla);
        return batallaRepository.save(batalla);
    }

    /**
     * Partially update a batalla.
     *
     * @param batalla the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Batalla> partialUpdate(Batalla batalla) {
        log.debug("Request to partially update Batalla : {}", batalla);

        return batallaRepository
            .findById(batalla.getId())
            .map(
                existingBatalla -> {
                    if (batalla.getNombre() != null) {
                        existingBatalla.setNombre(batalla.getNombre());
                    }
                    if (batalla.getPlaneta() != null) {
                        existingBatalla.setPlaneta(batalla.getPlaneta());
                    }

                    return existingBatalla;
                }
            )
            .map(batallaRepository::save);
    }

    public List<Batalla> getBatallaPorGanadorYPlaneta(String ganador, String planeta) {
        return batallaRepository.findByGanador_nombreAndPlaneta(ganador, planeta);
    }

    public List<Batalla> getBatallaPorInvolucrados(String nombre) {
        return batallaRepository.findByInvolucrados_nombre(nombre);
    }

    /**
     * Get all the batallas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Batalla> findAll() {
        log.debug("Request to get all Batallas");
        return batallaRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the batallas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Batalla> findAllWithEagerRelationships(Pageable pageable) {
        return batallaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one batalla by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Batalla> findOne(Long id) {
        log.debug("Request to get Batalla : {}", id);
        return batallaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the batalla by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Batalla : {}", id);
        batallaRepository.deleteById(id);
    }
}
