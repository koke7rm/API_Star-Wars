package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Bando;
import com.mycompany.myapp.repository.BandoRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link BandoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BandoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/bandos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BandoRepository bandoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBandoMockMvc;

    private Bando bando;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bando createEntity(EntityManager em) {
        Bando bando = new Bando().nombre(DEFAULT_NOMBRE).logo(DEFAULT_LOGO).logoContentType(DEFAULT_LOGO_CONTENT_TYPE);
        return bando;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bando createUpdatedEntity(EntityManager em) {
        Bando bando = new Bando().nombre(UPDATED_NOMBRE).logo(UPDATED_LOGO).logoContentType(UPDATED_LOGO_CONTENT_TYPE);
        return bando;
    }

    @BeforeEach
    public void initTest() {
        bando = createEntity(em);
    }

    @Test
    @Transactional
    void createBando() throws Exception {
        int databaseSizeBeforeCreate = bandoRepository.findAll().size();
        // Create the Bando
        restBandoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bando)))
            .andExpect(status().isCreated());

        // Validate the Bando in the database
        List<Bando> bandoList = bandoRepository.findAll();
        assertThat(bandoList).hasSize(databaseSizeBeforeCreate + 1);
        Bando testBando = bandoList.get(bandoList.size() - 1);
        assertThat(testBando.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testBando.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testBando.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createBandoWithExistingId() throws Exception {
        // Create the Bando with an existing ID
        bando.setId(1L);

        int databaseSizeBeforeCreate = bandoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBandoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bando)))
            .andExpect(status().isBadRequest());

        // Validate the Bando in the database
        List<Bando> bandoList = bandoRepository.findAll();
        assertThat(bandoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBandos() throws Exception {
        // Initialize the database
        bandoRepository.saveAndFlush(bando);

        // Get all the bandoList
        restBandoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bando.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))));
    }

    @Test
    @Transactional
    void getBando() throws Exception {
        // Initialize the database
        bandoRepository.saveAndFlush(bando);

        // Get the bando
        restBandoMockMvc
            .perform(get(ENTITY_API_URL_ID, bando.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bando.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)));
    }

    @Test
    @Transactional
    void getNonExistingBando() throws Exception {
        // Get the bando
        restBandoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBando() throws Exception {
        // Initialize the database
        bandoRepository.saveAndFlush(bando);

        int databaseSizeBeforeUpdate = bandoRepository.findAll().size();

        // Update the bando
        Bando updatedBando = bandoRepository.findById(bando.getId()).get();
        // Disconnect from session so that the updates on updatedBando are not directly saved in db
        em.detach(updatedBando);
        updatedBando.nombre(UPDATED_NOMBRE).logo(UPDATED_LOGO).logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restBandoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBando.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBando))
            )
            .andExpect(status().isOk());

        // Validate the Bando in the database
        List<Bando> bandoList = bandoRepository.findAll();
        assertThat(bandoList).hasSize(databaseSizeBeforeUpdate);
        Bando testBando = bandoList.get(bandoList.size() - 1);
        assertThat(testBando.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testBando.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testBando.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingBando() throws Exception {
        int databaseSizeBeforeUpdate = bandoRepository.findAll().size();
        bando.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBandoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bando.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bando))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bando in the database
        List<Bando> bandoList = bandoRepository.findAll();
        assertThat(bandoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBando() throws Exception {
        int databaseSizeBeforeUpdate = bandoRepository.findAll().size();
        bando.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBandoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bando))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bando in the database
        List<Bando> bandoList = bandoRepository.findAll();
        assertThat(bandoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBando() throws Exception {
        int databaseSizeBeforeUpdate = bandoRepository.findAll().size();
        bando.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBandoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bando)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bando in the database
        List<Bando> bandoList = bandoRepository.findAll();
        assertThat(bandoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBandoWithPatch() throws Exception {
        // Initialize the database
        bandoRepository.saveAndFlush(bando);

        int databaseSizeBeforeUpdate = bandoRepository.findAll().size();

        // Update the bando using partial update
        Bando partialUpdatedBando = new Bando();
        partialUpdatedBando.setId(bando.getId());

        restBandoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBando.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBando))
            )
            .andExpect(status().isOk());

        // Validate the Bando in the database
        List<Bando> bandoList = bandoRepository.findAll();
        assertThat(bandoList).hasSize(databaseSizeBeforeUpdate);
        Bando testBando = bandoList.get(bandoList.size() - 1);
        assertThat(testBando.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testBando.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testBando.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateBandoWithPatch() throws Exception {
        // Initialize the database
        bandoRepository.saveAndFlush(bando);

        int databaseSizeBeforeUpdate = bandoRepository.findAll().size();

        // Update the bando using partial update
        Bando partialUpdatedBando = new Bando();
        partialUpdatedBando.setId(bando.getId());

        partialUpdatedBando.nombre(UPDATED_NOMBRE).logo(UPDATED_LOGO).logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restBandoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBando.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBando))
            )
            .andExpect(status().isOk());

        // Validate the Bando in the database
        List<Bando> bandoList = bandoRepository.findAll();
        assertThat(bandoList).hasSize(databaseSizeBeforeUpdate);
        Bando testBando = bandoList.get(bandoList.size() - 1);
        assertThat(testBando.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testBando.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testBando.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingBando() throws Exception {
        int databaseSizeBeforeUpdate = bandoRepository.findAll().size();
        bando.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBandoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bando.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bando))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bando in the database
        List<Bando> bandoList = bandoRepository.findAll();
        assertThat(bandoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBando() throws Exception {
        int databaseSizeBeforeUpdate = bandoRepository.findAll().size();
        bando.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBandoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bando))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bando in the database
        List<Bando> bandoList = bandoRepository.findAll();
        assertThat(bandoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBando() throws Exception {
        int databaseSizeBeforeUpdate = bandoRepository.findAll().size();
        bando.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBandoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bando)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bando in the database
        List<Bando> bandoList = bandoRepository.findAll();
        assertThat(bandoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBando() throws Exception {
        // Initialize the database
        bandoRepository.saveAndFlush(bando);

        int databaseSizeBeforeDelete = bandoRepository.findAll().size();

        // Delete the bando
        restBandoMockMvc
            .perform(delete(ENTITY_API_URL_ID, bando.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bando> bandoList = bandoRepository.findAll();
        assertThat(bandoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
