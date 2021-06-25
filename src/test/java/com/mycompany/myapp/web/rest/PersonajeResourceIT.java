package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Personaje;
import com.mycompany.myapp.repository.PersonajeRepository;
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

/**
 * Integration tests for the {@link PersonajeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonajeResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_EDAD = 1;
    private static final Integer UPDATED_EDAD = 2;

    private static final String DEFAULT_RANGO = "AAAAAAAAAA";
    private static final String UPDATED_RANGO = "BBBBBBBBBB";

    private static final String DEFAULT_ESPECIE = "AAAAAAAAAA";
    private static final String UPDATED_ESPECIE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personajes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonajeRepository personajeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonajeMockMvc;

    private Personaje personaje;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personaje createEntity(EntityManager em) {
        Personaje personaje = new Personaje().nombre(DEFAULT_NOMBRE).edad(DEFAULT_EDAD).rango(DEFAULT_RANGO).especie(DEFAULT_ESPECIE);
        return personaje;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personaje createUpdatedEntity(EntityManager em) {
        Personaje personaje = new Personaje().nombre(UPDATED_NOMBRE).edad(UPDATED_EDAD).rango(UPDATED_RANGO).especie(UPDATED_ESPECIE);
        return personaje;
    }

    @BeforeEach
    public void initTest() {
        personaje = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonaje() throws Exception {
        int databaseSizeBeforeCreate = personajeRepository.findAll().size();
        // Create the Personaje
        restPersonajeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaje)))
            .andExpect(status().isCreated());

        // Validate the Personaje in the database
        List<Personaje> personajeList = personajeRepository.findAll();
        assertThat(personajeList).hasSize(databaseSizeBeforeCreate + 1);
        Personaje testPersonaje = personajeList.get(personajeList.size() - 1);
        assertThat(testPersonaje.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPersonaje.getEdad()).isEqualTo(DEFAULT_EDAD);
        assertThat(testPersonaje.getRango()).isEqualTo(DEFAULT_RANGO);
        assertThat(testPersonaje.getEspecie()).isEqualTo(DEFAULT_ESPECIE);
    }

    @Test
    @Transactional
    void createPersonajeWithExistingId() throws Exception {
        // Create the Personaje with an existing ID
        personaje.setId(1L);

        int databaseSizeBeforeCreate = personajeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonajeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaje)))
            .andExpect(status().isBadRequest());

        // Validate the Personaje in the database
        List<Personaje> personajeList = personajeRepository.findAll();
        assertThat(personajeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonajes() throws Exception {
        // Initialize the database
        personajeRepository.saveAndFlush(personaje);

        // Get all the personajeList
        restPersonajeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personaje.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD)))
            .andExpect(jsonPath("$.[*].rango").value(hasItem(DEFAULT_RANGO)))
            .andExpect(jsonPath("$.[*].especie").value(hasItem(DEFAULT_ESPECIE)));
    }

    @Test
    @Transactional
    void getPersonaje() throws Exception {
        // Initialize the database
        personajeRepository.saveAndFlush(personaje);

        // Get the personaje
        restPersonajeMockMvc
            .perform(get(ENTITY_API_URL_ID, personaje.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personaje.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.edad").value(DEFAULT_EDAD))
            .andExpect(jsonPath("$.rango").value(DEFAULT_RANGO))
            .andExpect(jsonPath("$.especie").value(DEFAULT_ESPECIE));
    }

    @Test
    @Transactional
    void getNonExistingPersonaje() throws Exception {
        // Get the personaje
        restPersonajeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonaje() throws Exception {
        // Initialize the database
        personajeRepository.saveAndFlush(personaje);

        int databaseSizeBeforeUpdate = personajeRepository.findAll().size();

        // Update the personaje
        Personaje updatedPersonaje = personajeRepository.findById(personaje.getId()).get();
        // Disconnect from session so that the updates on updatedPersonaje are not directly saved in db
        em.detach(updatedPersonaje);
        updatedPersonaje.nombre(UPDATED_NOMBRE).edad(UPDATED_EDAD).rango(UPDATED_RANGO).especie(UPDATED_ESPECIE);

        restPersonajeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonaje.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonaje))
            )
            .andExpect(status().isOk());

        // Validate the Personaje in the database
        List<Personaje> personajeList = personajeRepository.findAll();
        assertThat(personajeList).hasSize(databaseSizeBeforeUpdate);
        Personaje testPersonaje = personajeList.get(personajeList.size() - 1);
        assertThat(testPersonaje.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPersonaje.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testPersonaje.getRango()).isEqualTo(UPDATED_RANGO);
        assertThat(testPersonaje.getEspecie()).isEqualTo(UPDATED_ESPECIE);
    }

    @Test
    @Transactional
    void putNonExistingPersonaje() throws Exception {
        int databaseSizeBeforeUpdate = personajeRepository.findAll().size();
        personaje.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonajeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personaje.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personaje))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personaje in the database
        List<Personaje> personajeList = personajeRepository.findAll();
        assertThat(personajeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonaje() throws Exception {
        int databaseSizeBeforeUpdate = personajeRepository.findAll().size();
        personaje.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonajeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personaje))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personaje in the database
        List<Personaje> personajeList = personajeRepository.findAll();
        assertThat(personajeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonaje() throws Exception {
        int databaseSizeBeforeUpdate = personajeRepository.findAll().size();
        personaje.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonajeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaje)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personaje in the database
        List<Personaje> personajeList = personajeRepository.findAll();
        assertThat(personajeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonajeWithPatch() throws Exception {
        // Initialize the database
        personajeRepository.saveAndFlush(personaje);

        int databaseSizeBeforeUpdate = personajeRepository.findAll().size();

        // Update the personaje using partial update
        Personaje partialUpdatedPersonaje = new Personaje();
        partialUpdatedPersonaje.setId(personaje.getId());

        partialUpdatedPersonaje.edad(UPDATED_EDAD).rango(UPDATED_RANGO);

        restPersonajeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonaje.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonaje))
            )
            .andExpect(status().isOk());

        // Validate the Personaje in the database
        List<Personaje> personajeList = personajeRepository.findAll();
        assertThat(personajeList).hasSize(databaseSizeBeforeUpdate);
        Personaje testPersonaje = personajeList.get(personajeList.size() - 1);
        assertThat(testPersonaje.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPersonaje.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testPersonaje.getRango()).isEqualTo(UPDATED_RANGO);
        assertThat(testPersonaje.getEspecie()).isEqualTo(DEFAULT_ESPECIE);
    }

    @Test
    @Transactional
    void fullUpdatePersonajeWithPatch() throws Exception {
        // Initialize the database
        personajeRepository.saveAndFlush(personaje);

        int databaseSizeBeforeUpdate = personajeRepository.findAll().size();

        // Update the personaje using partial update
        Personaje partialUpdatedPersonaje = new Personaje();
        partialUpdatedPersonaje.setId(personaje.getId());

        partialUpdatedPersonaje.nombre(UPDATED_NOMBRE).edad(UPDATED_EDAD).rango(UPDATED_RANGO).especie(UPDATED_ESPECIE);

        restPersonajeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonaje.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonaje))
            )
            .andExpect(status().isOk());

        // Validate the Personaje in the database
        List<Personaje> personajeList = personajeRepository.findAll();
        assertThat(personajeList).hasSize(databaseSizeBeforeUpdate);
        Personaje testPersonaje = personajeList.get(personajeList.size() - 1);
        assertThat(testPersonaje.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPersonaje.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testPersonaje.getRango()).isEqualTo(UPDATED_RANGO);
        assertThat(testPersonaje.getEspecie()).isEqualTo(UPDATED_ESPECIE);
    }

    @Test
    @Transactional
    void patchNonExistingPersonaje() throws Exception {
        int databaseSizeBeforeUpdate = personajeRepository.findAll().size();
        personaje.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonajeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personaje.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personaje))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personaje in the database
        List<Personaje> personajeList = personajeRepository.findAll();
        assertThat(personajeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonaje() throws Exception {
        int databaseSizeBeforeUpdate = personajeRepository.findAll().size();
        personaje.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonajeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personaje))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personaje in the database
        List<Personaje> personajeList = personajeRepository.findAll();
        assertThat(personajeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonaje() throws Exception {
        int databaseSizeBeforeUpdate = personajeRepository.findAll().size();
        personaje.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonajeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personaje))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personaje in the database
        List<Personaje> personajeList = personajeRepository.findAll();
        assertThat(personajeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonaje() throws Exception {
        // Initialize the database
        personajeRepository.saveAndFlush(personaje);

        int databaseSizeBeforeDelete = personajeRepository.findAll().size();

        // Delete the personaje
        restPersonajeMockMvc
            .perform(delete(ENTITY_API_URL_ID, personaje.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Personaje> personajeList = personajeRepository.findAll();
        assertThat(personajeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
