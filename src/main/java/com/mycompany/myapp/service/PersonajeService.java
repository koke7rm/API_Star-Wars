package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Batalla;
import com.mycompany.myapp.domain.Personaje;
import com.mycompany.myapp.repository.PersonajeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Personaje}.
 */
@Service
@Transactional
public class PersonajeService {

    private final Logger log = LoggerFactory.getLogger(PersonajeService.class);

    private final PersonajeRepository personajeRepository;

    public PersonajeService(PersonajeRepository personajeRepository) {
        this.personajeRepository = personajeRepository;
    }

    /**
     * Save a personaje.
     *
     * @param personaje the entity to save.
     * @return the persisted entity.
     */
    public Personaje save(Personaje personaje) {
        log.debug("Request to save Personaje : {}", personaje);
        return personajeRepository.save(personaje);
    }

    /**
     * Partially update a personaje.
     *
     * @param personaje the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Personaje> partialUpdate(Personaje personaje) {
        log.debug("Request to partially update Personaje : {}", personaje);

        return personajeRepository
            .findById(personaje.getId())
            .map(
                existingPersonaje -> {
                    if (personaje.getNombre() != null) {
                        existingPersonaje.setNombre(personaje.getNombre());
                    }
                    if (personaje.getEdad() != null) {
                        existingPersonaje.setEdad(personaje.getEdad());
                    }
                    if (personaje.getRango() != null) {
                        existingPersonaje.setRango(personaje.getRango());
                    }
                    if (personaje.getEspecie() != null) {
                        existingPersonaje.setEspecie(personaje.getEspecie());
                    }

                    return existingPersonaje;
                }
            )
            .map(personajeRepository::save);
    }

    public List<Personaje> getPersonajesIvolucrados(String nombre) {
        return personajeRepository.findAllByInvolucrados_nombre(nombre);
    }

    public Optional<Personaje> getPersonajePorNombre(String nombre) {
        return personajeRepository.findByNombre(nombre);
    }

    public List<Personaje> getPersonajesDeUnBando(String nombre) {
        return personajeRepository.findAllByIntegrantes_nombre(nombre);
    }

    /**
     * Get all the personajes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Personaje> findAll() {
        log.debug("Request to get all Personajes");
        return personajeRepository.findAll();
    }

    /**
     * Get one personaje by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Personaje> findOne(Long id) {
        log.debug("Request to get Personaje : {}", id);
        return personajeRepository.findById(id);
    }

    /**
     * Delete the personaje by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Personaje : {}", id);
        personajeRepository.deleteById(id);
    }
}
