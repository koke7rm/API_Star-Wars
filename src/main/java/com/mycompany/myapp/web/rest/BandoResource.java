package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Bando;
import com.mycompany.myapp.repository.BandoRepository;
import com.mycompany.myapp.service.BandoService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Bando}.
 */
@RestController
@RequestMapping("/api")
public class BandoResource {

    private final Logger log = LoggerFactory.getLogger(BandoResource.class);

    private static final String ENTITY_NAME = "bando";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BandoService bandoService;

    private final BandoRepository bandoRepository;

    public BandoResource(BandoService bandoService, BandoRepository bandoRepository) {
        this.bandoService = bandoService;
        this.bandoRepository = bandoRepository;
    }

    /**
     * {@code POST  /bandos} : Create a new bando.
     *
     * @param bando the bando to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bando, or with status {@code 400 (Bad Request)} if the bando has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bandos")
    public ResponseEntity<Bando> createBando(@Valid @RequestBody Bando bando) throws URISyntaxException {
        log.debug("REST request to save Bando : {}", bando);
        if (bando.getId() != null) {
            throw new BadRequestAlertException("A new bando cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bando result = bandoService.save(bando);
        return ResponseEntity
            .created(new URI("/api/bandos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bandos/:id} : Updates an existing bando.
     *
     * @param id the id of the bando to save.
     * @param bando the bando to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bando,
     * or with status {@code 400 (Bad Request)} if the bando is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bando couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bandos/{id}")
    public ResponseEntity<Bando> updateBando(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Bando bando)
        throws URISyntaxException {
        log.debug("REST request to update Bando : {}, {}", id, bando);
        if (bando.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bando.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bandoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Bando result = bandoService.save(bando);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bando.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bandos/:id} : Partial updates given fields of an existing bando, field will ignore if it is null
     *
     * @param id the id of the bando to save.
     * @param bando the bando to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bando,
     * or with status {@code 400 (Bad Request)} if the bando is not valid,
     * or with status {@code 404 (Not Found)} if the bando is not found,
     * or with status {@code 500 (Internal Server Error)} if the bando couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bandos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Bando> partialUpdateBando(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Bando bando
    ) throws URISyntaxException {
        log.debug("REST request to partial update Bando partially : {}, {}", id, bando);
        if (bando.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bando.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bandoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Bando> result = bandoService.partialUpdate(bando);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bando.getId().toString())
        );
    }

    /**
     * {@code GET  /bandos} : get all the bandos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bandos in body.
     */
    @GetMapping("/bandos")
    public List<Bando> getAllBandos() {
        log.debug("REST request to get all Bandos");
        return bandoService.findAll();
    }

    /**
     * {@code GET  /bandos/:id} : get the "id" bando.
     *
     * @param id the id of the bando to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bando, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bandos/{id}")
    public ResponseEntity<Bando> getBando(@PathVariable Long id) {
        log.debug("REST request to get Bando : {}", id);
        Optional<Bando> bando = bandoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bando);
    }

    /**
     * {@code DELETE  /bandos/:id} : delete the "id" bando.
     *
     * @param id the id of the bando to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bandos/{id}")
    public ResponseEntity<Void> deleteBando(@PathVariable Long id) {
        log.debug("REST request to delete Bando : {}", id);
        bandoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
