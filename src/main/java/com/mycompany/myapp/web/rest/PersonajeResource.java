package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Batalla;
import com.mycompany.myapp.domain.Personaje;
import com.mycompany.myapp.repository.PersonajeRepository;
import com.mycompany.myapp.service.PersonajeService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Personaje}.
 */
@RestController
@RequestMapping("/api")
public class PersonajeResource {

    private final Logger log = LoggerFactory.getLogger(PersonajeResource.class);

    private static final String ENTITY_NAME = "personaje";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonajeService personajeService;

    private final PersonajeRepository personajeRepository;

    public PersonajeResource(PersonajeService personajeService, PersonajeRepository personajeRepository) {
        this.personajeService = personajeService;
        this.personajeRepository = personajeRepository;
    }

    /**
     * {@code POST  /personajes} : Create a new personaje.
     *
     * @param personaje the personaje to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personaje, or with status {@code 400 (Bad Request)} if the personaje has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personajes")
    public ResponseEntity<Personaje> createPersonaje(@Valid @RequestBody Personaje personaje) throws URISyntaxException {
        log.debug("REST request to save Personaje : {}", personaje);
        if (personaje.getId() != null) {
            throw new BadRequestAlertException("A new personaje cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Personaje result = personajeService.save(personaje);
        return ResponseEntity
            .created(new URI("/api/personajes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personajes/:id} : Updates an existing personaje.
     *
     * @param id the id of the personaje to save.
     * @param personaje the personaje to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personaje,
     * or with status {@code 400 (Bad Request)} if the personaje is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personaje couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personajes/{id}")
    public ResponseEntity<Personaje> updatePersonaje(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Personaje personaje
    ) throws URISyntaxException {
        log.debug("REST request to update Personaje : {}, {}", id, personaje);
        if (personaje.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personaje.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personajeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Personaje result = personajeService.save(personaje);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personaje.getId().toString()))
            .body(result);
    }

    @GetMapping(path = "personajesinvolucrados/{nombre}")
    public List<Personaje> getPersonajesIvolucrados(@PathVariable(name = "nombre") String nombre) {
        return personajeService.getPersonajesIvolucrados(nombre);
    }

    @GetMapping(path = "personajepornombre/{nombre}")
    public Optional<Personaje> getPersonajePorNombre(@PathVariable(name = "nombre") String nombre) {
        return personajeService.getPersonajePorNombre(nombre);
    }

    @GetMapping(path = "personajesdeunbando/{nombre}")
    public List<Personaje> getPersonajesDeUnBando(@PathVariable(name = "nombre") String nombre) {
        return personajeService.getPersonajesDeUnBando(nombre);
    }

    /**
     * {@code PATCH  /personajes/:id} : Partial updates given fields of an existing personaje, field will ignore if it is null
     *
     * @param id the id of the personaje to save.
     * @param personaje the personaje to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personaje,
     * or with status {@code 400 (Bad Request)} if the personaje is not valid,
     * or with status {@code 404 (Not Found)} if the personaje is not found,
     * or with status {@code 500 (Internal Server Error)} if the personaje couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/personajes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Personaje> partialUpdatePersonaje(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Personaje personaje
    ) throws URISyntaxException {
        log.debug("REST request to partial update Personaje partially : {}, {}", id, personaje);
        if (personaje.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personaje.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personajeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Personaje> result = personajeService.partialUpdate(personaje);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personaje.getId().toString())
        );
    }

    /**
     * {@code GET  /personajes} : get all the personajes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personajes in body.
     */
    @GetMapping("/personajes")
    public List<Personaje> getAllPersonajes() {
        log.debug("REST request to get all Personajes");
        return personajeService.findAll();
    }

    /**
     * {@code GET  /personajes/:id} : get the "id" personaje.
     *
     * @param id the id of the personaje to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personaje, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personajes/{id}")
    public ResponseEntity<Personaje> getPersonaje(@PathVariable Long id) {
        log.debug("REST request to get Personaje : {}", id);
        Optional<Personaje> personaje = personajeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personaje);
    }

    /**
     * {@code DELETE  /personajes/:id} : delete the "id" personaje.
     *
     * @param id the id of the personaje to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personajes/{id}")
    public ResponseEntity<Void> deletePersonaje(@PathVariable Long id) {
        log.debug("REST request to delete Personaje : {}", id);
        personajeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
