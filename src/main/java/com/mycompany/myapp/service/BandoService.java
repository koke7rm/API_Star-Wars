package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Bando;
import com.mycompany.myapp.repository.BandoRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Bando}.
 */
@Service
@Transactional
public class BandoService {

    private final Logger log = LoggerFactory.getLogger(BandoService.class);

    private final BandoRepository bandoRepository;

    public BandoService(BandoRepository bandoRepository) {
        this.bandoRepository = bandoRepository;
    }

    /**
     * Save a bando.
     *
     * @param bando the entity to save.
     * @return the persisted entity.
     */
    public Bando save(Bando bando) {
        log.debug("Request to save Bando : {}", bando);
        return bandoRepository.save(bando);
    }

    /**
     * Partially update a bando.
     *
     * @param bando the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Bando> partialUpdate(Bando bando) {
        log.debug("Request to partially update Bando : {}", bando);

        return bandoRepository
            .findById(bando.getId())
            .map(
                existingBando -> {
                    if (bando.getNombre() != null) {
                        existingBando.setNombre(bando.getNombre());
                    }
                    if (bando.getLogo() != null) {
                        existingBando.setLogo(bando.getLogo());
                    }
                    if (bando.getLogoContentType() != null) {
                        existingBando.setLogoContentType(bando.getLogoContentType());
                    }

                    return existingBando;
                }
            )
            .map(bandoRepository::save);
    }

    /**
     * Get all the bandos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Bando> findAll() {
        log.debug("Request to get all Bandos");
        return bandoRepository.findAll();
    }

    /**
     * Get one bando by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Bando> findOne(Long id) {
        log.debug("Request to get Bando : {}", id);
        return bandoRepository.findById(id);
    }

    /**
     * Delete the bando by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Bando : {}", id);
        bandoRepository.deleteById(id);
    }
}
