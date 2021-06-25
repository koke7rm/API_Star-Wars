package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Batalla;
import com.mycompany.myapp.repository.BatallaRepository;
import com.mycompany.myapp.service.BatallaService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BatallaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BatallaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PLANETA = "AAAAAAAAAA";
    private static final String UPDATED_PLANETA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/batallas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BatallaRepository batallaRepository;

    @Mock
    private BatallaRepository batallaRepositoryMock;

    @Mock
    private BatallaService batallaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBatallaMockMvc;

    private Batalla batalla;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batalla createEntity(EntityManager em) {
        Batalla batalla = new Batalla().nombre(DEFAULT_NOMBRE).planeta(DEFAULT_PLANETA);
        return batalla;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batalla createUpdatedEntity(EntityManager em) {
        Batalla batalla = new Batalla().nombre(UPDATED_NOMBRE).planeta(UPDATED_PLANETA);
        return batalla;
    }

    @BeforeEach
    public void initTest() {
        batalla = createEntity(em);
    }

    @Test
    @Transactional
    void createBatalla() throws Exception {
        int databaseSizeBeforeCreate = batallaRepository.findAll().size();
        // Create the Batalla
        restBatallaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batalla)))
            .andExpect(status().isCreated());

        // Validate the Batalla in the database
        List<Batalla> batallaList = batallaRepository.findAll();
        assertThat(batallaList).hasSize(databaseSizeBeforeCreate + 1);
        Batalla testBatalla = batallaList.get(batallaList.size() - 1);
        assertThat(testBatalla.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testBatalla.getPlaneta()).isEqualTo(DEFAULT_PLANETA);
    }

    @Test
    @Transactional
    void createBatallaWithExistingId() throws Exception {
        // Create the Batalla with an existing ID
        batalla.setId(1L);

        int databaseSizeBeforeCreate = batallaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBatallaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batalla)))
            .andExpect(status().isBadRequest());

        // Validate the Batalla in the database
        List<Batalla> batallaList = batallaRepository.findAll();
        assertThat(batallaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBatallas() throws Exception {
        // Initialize the database
        batallaRepository.saveAndFlush(batalla);

        // Get all the batallaList
        restBatallaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batalla.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].planeta").value(hasItem(DEFAULT_PLANETA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBatallasWithEagerRelationshipsIsEnabled() throws Exception {
        when(batallaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBatallaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(batallaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBatallasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(batallaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBatallaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(batallaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBatalla() throws Exception {
        // Initialize the database
        batallaRepository.saveAndFlush(batalla);

        // Get the batalla
        restBatallaMockMvc
            .perform(get(ENTITY_API_URL_ID, batalla.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(batalla.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.planeta").value(DEFAULT_PLANETA));
    }

    @Test
    @Transactional
    void getNonExistingBatalla() throws Exception {
        // Get the batalla
        restBatallaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBatalla() throws Exception {
        // Initialize the database
        batallaRepository.saveAndFlush(batalla);

        int databaseSizeBeforeUpdate = batallaRepository.findAll().size();

        // Update the batalla
        Batalla updatedBatalla = batallaRepository.findById(batalla.getId()).get();
        // Disconnect from session so that the updates on updatedBatalla are not directly saved in db
        em.detach(updatedBatalla);
        updatedBatalla.nombre(UPDATED_NOMBRE).planeta(UPDATED_PLANETA);

        restBatallaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBatalla.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBatalla))
            )
            .andExpect(status().isOk());

        // Validate the Batalla in the database
        List<Batalla> batallaList = batallaRepository.findAll();
        assertThat(batallaList).hasSize(databaseSizeBeforeUpdate);
        Batalla testBatalla = batallaList.get(batallaList.size() - 1);
        assertThat(testBatalla.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testBatalla.getPlaneta()).isEqualTo(UPDATED_PLANETA);
    }

    @Test
    @Transactional
    void putNonExistingBatalla() throws Exception {
        int databaseSizeBeforeUpdate = batallaRepository.findAll().size();
        batalla.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatallaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, batalla.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batalla))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batalla in the database
        List<Batalla> batallaList = batallaRepository.findAll();
        assertThat(batallaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBatalla() throws Exception {
        int databaseSizeBeforeUpdate = batallaRepository.findAll().size();
        batalla.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatallaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batalla))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batalla in the database
        List<Batalla> batallaList = batallaRepository.findAll();
        assertThat(batallaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBatalla() throws Exception {
        int databaseSizeBeforeUpdate = batallaRepository.findAll().size();
        batalla.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatallaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batalla)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Batalla in the database
        List<Batalla> batallaList = batallaRepository.findAll();
        assertThat(batallaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBatallaWithPatch() throws Exception {
        // Initialize the database
        batallaRepository.saveAndFlush(batalla);

        int databaseSizeBeforeUpdate = batallaRepository.findAll().size();

        // Update the batalla using partial update
        Batalla partialUpdatedBatalla = new Batalla();
        partialUpdatedBatalla.setId(batalla.getId());

        partialUpdatedBatalla.planeta(UPDATED_PLANETA);

        restBatallaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatalla.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBatalla))
            )
            .andExpect(status().isOk());

        // Validate the Batalla in the database
        List<Batalla> batallaList = batallaRepository.findAll();
        assertThat(batallaList).hasSize(databaseSizeBeforeUpdate);
        Batalla testBatalla = batallaList.get(batallaList.size() - 1);
        assertThat(testBatalla.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testBatalla.getPlaneta()).isEqualTo(UPDATED_PLANETA);
    }

    @Test
    @Transactional
    void fullUpdateBatallaWithPatch() throws Exception {
        // Initialize the database
        batallaRepository.saveAndFlush(batalla);

        int databaseSizeBeforeUpdate = batallaRepository.findAll().size();

        // Update the batalla using partial update
        Batalla partialUpdatedBatalla = new Batalla();
        partialUpdatedBatalla.setId(batalla.getId());

        partialUpdatedBatalla.nombre(UPDATED_NOMBRE).planeta(UPDATED_PLANETA);

        restBatallaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatalla.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBatalla))
            )
            .andExpect(status().isOk());

        // Validate the Batalla in the database
        List<Batalla> batallaList = batallaRepository.findAll();
        assertThat(batallaList).hasSize(databaseSizeBeforeUpdate);
        Batalla testBatalla = batallaList.get(batallaList.size() - 1);
        assertThat(testBatalla.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testBatalla.getPlaneta()).isEqualTo(UPDATED_PLANETA);
    }

    @Test
    @Transactional
    void patchNonExistingBatalla() throws Exception {
        int databaseSizeBeforeUpdate = batallaRepository.findAll().size();
        batalla.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatallaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, batalla.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(batalla))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batalla in the database
        List<Batalla> batallaList = batallaRepository.findAll();
        assertThat(batallaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBatalla() throws Exception {
        int databaseSizeBeforeUpdate = batallaRepository.findAll().size();
        batalla.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatallaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(batalla))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batalla in the database
        List<Batalla> batallaList = batallaRepository.findAll();
        assertThat(batallaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBatalla() throws Exception {
        int databaseSizeBeforeUpdate = batallaRepository.findAll().size();
        batalla.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatallaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(batalla)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Batalla in the database
        List<Batalla> batallaList = batallaRepository.findAll();
        assertThat(batallaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBatalla() throws Exception {
        // Initialize the database
        batallaRepository.saveAndFlush(batalla);

        int databaseSizeBeforeDelete = batallaRepository.findAll().size();

        // Delete the batalla
        restBatallaMockMvc
            .perform(delete(ENTITY_API_URL_ID, batalla.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Batalla> batallaList = batallaRepository.findAll();
        assertThat(batallaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
