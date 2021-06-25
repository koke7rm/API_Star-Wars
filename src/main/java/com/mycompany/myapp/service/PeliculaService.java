package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Pelicula;
import com.mycompany.myapp.repository.PeliculaRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pelicula}.
 */
@Service
@Transactional
public class PeliculaService {

    private final Logger log = LoggerFactory.getLogger(PeliculaService.class);

    private final PeliculaRepository peliculaRepository;

    public PeliculaService(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    /**
     * Save a pelicula.
     *
     * @param pelicula the entity to save.
     * @return the persisted entity.
     */
    public Pelicula save(Pelicula pelicula) {
        log.debug("Request to save Pelicula : {}", pelicula);
        return peliculaRepository.save(pelicula);
    }

    /**
     * Partially update a pelicula.
     *
     * @param pelicula the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Pelicula> partialUpdate(Pelicula pelicula) {
        log.debug("Request to partially update Pelicula : {}", pelicula);

        return peliculaRepository
            .findById(pelicula.getId())
            .map(
                existingPelicula -> {
                    if (pelicula.getTitulo() != null) {
                        existingPelicula.setTitulo(pelicula.getTitulo());
                    }
                    if (pelicula.getEpisodio() != null) {
                        existingPelicula.setEpisodio(pelicula.getEpisodio());
                    }
                    if (pelicula.getEstreno() != null) {
                        existingPelicula.setEstreno(pelicula.getEstreno());
                    }

                    return existingPelicula;
                }
            )
            .map(peliculaRepository::save);
    }

    /**
     * Get all the peliculas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Pelicula> findAll() {
        log.debug("Request to get all Peliculas");
        return peliculaRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the peliculas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Pelicula> findAllWithEagerRelationships(Pageable pageable) {
        return peliculaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one pelicula by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Pelicula> findOne(Long id) {
        log.debug("Request to get Pelicula : {}", id);
        return peliculaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the pelicula by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pelicula : {}", id);
        peliculaRepository.deleteById(id);
    }
}
