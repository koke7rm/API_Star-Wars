package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Batalla;
import com.mycompany.myapp.repository.BatallaRepository;
import com.mycompany.myapp.service.BatallaService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Batalla}.
 */
@RestController
@RequestMapping("/api")
public class BatallaResource {

    private final Logger log = LoggerFactory.getLogger(BatallaResource.class);

    private static final String ENTITY_NAME = "batalla";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BatallaService batallaService;

    private final BatallaRepository batallaRepository;

    public BatallaResource(BatallaService batallaService, BatallaRepository batallaRepository) {
        this.batallaService = batallaService;
        this.batallaRepository = batallaRepository;
    }

    /**
     * {@code POST  /batallas} : Create a new batalla.
     *
     * @param batalla the batalla to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new batalla, or with status {@code 400 (Bad Request)} if the batalla has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/batallas")
    public ResponseEntity<Batalla> createBatalla(@Valid @RequestBody Batalla batalla) throws URISyntaxException {
        log.debug("REST request to save Batalla : {}", batalla);
        if (batalla.getId() != null) {
            throw new BadRequestAlertException("A new batalla cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Batalla result = batallaService.save(batalla);
        return ResponseEntity
            .created(new URI("/api/batallas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /batallas/:id} : Updates an existing batalla.
     *
     * @param id the id of the batalla to save.
     * @param batalla the batalla to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated batalla,
     * or with status {@code 400 (Bad Request)} if the batalla is not valid,
     * or with status {@code 500 (Internal Server Error)} if the batalla couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/batallas/{id}")
    public ResponseEntity<Batalla> updateBatalla(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Batalla batalla
    ) throws URISyntaxException {
        log.debug("REST request to update Batalla : {}, {}", id, batalla);
        if (batalla.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, batalla.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!batallaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Batalla result = batallaService.save(batalla);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, batalla.getId().toString()))
            .body(result);
    }

    @GetMapping(path = "batallaporganadoryplaneta/{ganador}/{planeta}")
    public List<Batalla> getBatallaPorGanadorYPlaneta(
        @PathVariable(name = "ganador") String ganador,
        @PathVariable(name = "planeta") String planeta
    ) {
        return batallaService.getBatallaPorGanadorYPlaneta(ganador, planeta);
    }

    @GetMapping(path = "batallaporinvolucrados/{nombre}")
    public List<Batalla> getBatallaPorInvolucrados(@PathVariable(name = "nombre") String nombre) {
        return batallaService.getBatallaPorInvolucrados(nombre);
    }

    /**
     * {@code PATCH  /batallas/:id} : Partial updates given fields of an existing batalla, field will ignore if it is null
     *
     * @param id the id of the batalla to save.
     * @param batalla the batalla to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated batalla,
     * or with status {@code 400 (Bad Request)} if the batalla is not valid,
     * or with status {@code 404 (Not Found)} if the batalla is not found,
     * or with status {@code 500 (Internal Server Error)} if the batalla couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/batallas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Batalla> partialUpdateBatalla(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Batalla batalla
    ) throws URISyntaxException {
        log.debug("REST request to partial update Batalla partially : {}, {}", id, batalla);
        if (batalla.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, batalla.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!batallaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Batalla> result = batallaService.partialUpdate(batalla);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, batalla.getId().toString())
        );
    }

    /**
     * {@code GET  /batallas} : get all the batallas.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of batallas in body.
     */
    @GetMapping("/batallas")
    public List<Batalla> getAllBatallas(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Batallas");
        return batallaService.findAll();
    }

    /**
     * {@code GET  /batallas/:id} : get the "id" batalla.
     *
     * @param id the id of the batalla to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the batalla, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/batallas/{id}")
    public ResponseEntity<Batalla> getBatalla(@PathVariable Long id) {
        log.debug("REST request to get Batalla : {}", id);
        Optional<Batalla> batalla = batallaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(batalla);
    }

    /**
     * {@code DELETE  /batallas/:id} : delete the "id" batalla.
     *
     * @param id the id of the batalla to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/batallas/{id}")
    public ResponseEntity<Void> deleteBatalla(@PathVariable Long id) {
        log.debug("REST request to delete Batalla : {}", id);
        batallaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
