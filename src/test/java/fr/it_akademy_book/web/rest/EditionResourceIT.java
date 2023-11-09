package fr.it_akademy_book.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.it_akademy_book.IntegrationTest;
import fr.it_akademy_book.domain.Edition;
import fr.it_akademy_book.repository.EditionRepository;
import fr.it_akademy_book.service.dto.EditionDTO;
import fr.it_akademy_book.service.mapper.EditionMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EditionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EditionResourceIT {

    private static final Long DEFAULT_DAY_OF_PUBLICATION = 1L;
    private static final Long UPDATED_DAY_OF_PUBLICATION = 2L;

    private static final String DEFAULT_MONTH_OF_PUBLICATION = "AAAAAAAAAA";
    private static final String UPDATED_MONTH_OF_PUBLICATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/editions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EditionRepository editionRepository;

    @Autowired
    private EditionMapper editionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEditionMockMvc;

    private Edition edition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Edition createEntity(EntityManager em) {
        Edition edition = new Edition().dayOfPublication(DEFAULT_DAY_OF_PUBLICATION).monthOfPublication(DEFAULT_MONTH_OF_PUBLICATION);
        return edition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Edition createUpdatedEntity(EntityManager em) {
        Edition edition = new Edition().dayOfPublication(UPDATED_DAY_OF_PUBLICATION).monthOfPublication(UPDATED_MONTH_OF_PUBLICATION);
        return edition;
    }

    @BeforeEach
    public void initTest() {
        edition = createEntity(em);
    }

    @Test
    @Transactional
    void createEdition() throws Exception {
        int databaseSizeBeforeCreate = editionRepository.findAll().size();
        // Create the Edition
        EditionDTO editionDTO = editionMapper.toDto(edition);
        restEditionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(editionDTO)))
            .andExpect(status().isCreated());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeCreate + 1);
        Edition testEdition = editionList.get(editionList.size() - 1);
        assertThat(testEdition.getDayOfPublication()).isEqualTo(DEFAULT_DAY_OF_PUBLICATION);
        assertThat(testEdition.getMonthOfPublication()).isEqualTo(DEFAULT_MONTH_OF_PUBLICATION);
    }

    @Test
    @Transactional
    void createEditionWithExistingId() throws Exception {
        // Create the Edition with an existing ID
        edition.setId(1L);
        EditionDTO editionDTO = editionMapper.toDto(edition);

        int databaseSizeBeforeCreate = editionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEditionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(editionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEditions() throws Exception {
        // Initialize the database
        editionRepository.saveAndFlush(edition);

        // Get all the editionList
        restEditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edition.getId().intValue())))
            .andExpect(jsonPath("$.[*].dayOfPublication").value(hasItem(DEFAULT_DAY_OF_PUBLICATION.intValue())))
            .andExpect(jsonPath("$.[*].monthOfPublication").value(hasItem(DEFAULT_MONTH_OF_PUBLICATION)));
    }

    @Test
    @Transactional
    void getEdition() throws Exception {
        // Initialize the database
        editionRepository.saveAndFlush(edition);

        // Get the edition
        restEditionMockMvc
            .perform(get(ENTITY_API_URL_ID, edition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(edition.getId().intValue()))
            .andExpect(jsonPath("$.dayOfPublication").value(DEFAULT_DAY_OF_PUBLICATION.intValue()))
            .andExpect(jsonPath("$.monthOfPublication").value(DEFAULT_MONTH_OF_PUBLICATION));
    }

    @Test
    @Transactional
    void getNonExistingEdition() throws Exception {
        // Get the edition
        restEditionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEdition() throws Exception {
        // Initialize the database
        editionRepository.saveAndFlush(edition);

        int databaseSizeBeforeUpdate = editionRepository.findAll().size();

        // Update the edition
        Edition updatedEdition = editionRepository.findById(edition.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEdition are not directly saved in db
        em.detach(updatedEdition);
        updatedEdition.dayOfPublication(UPDATED_DAY_OF_PUBLICATION).monthOfPublication(UPDATED_MONTH_OF_PUBLICATION);
        EditionDTO editionDTO = editionMapper.toDto(updatedEdition);

        restEditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, editionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(editionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeUpdate);
        Edition testEdition = editionList.get(editionList.size() - 1);
        assertThat(testEdition.getDayOfPublication()).isEqualTo(UPDATED_DAY_OF_PUBLICATION);
        assertThat(testEdition.getMonthOfPublication()).isEqualTo(UPDATED_MONTH_OF_PUBLICATION);
    }

    @Test
    @Transactional
    void putNonExistingEdition() throws Exception {
        int databaseSizeBeforeUpdate = editionRepository.findAll().size();
        edition.setId(longCount.incrementAndGet());

        // Create the Edition
        EditionDTO editionDTO = editionMapper.toDto(edition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, editionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(editionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEdition() throws Exception {
        int databaseSizeBeforeUpdate = editionRepository.findAll().size();
        edition.setId(longCount.incrementAndGet());

        // Create the Edition
        EditionDTO editionDTO = editionMapper.toDto(edition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(editionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEdition() throws Exception {
        int databaseSizeBeforeUpdate = editionRepository.findAll().size();
        edition.setId(longCount.incrementAndGet());

        // Create the Edition
        EditionDTO editionDTO = editionMapper.toDto(edition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(editionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEditionWithPatch() throws Exception {
        // Initialize the database
        editionRepository.saveAndFlush(edition);

        int databaseSizeBeforeUpdate = editionRepository.findAll().size();

        // Update the edition using partial update
        Edition partialUpdatedEdition = new Edition();
        partialUpdatedEdition.setId(edition.getId());

        partialUpdatedEdition.dayOfPublication(UPDATED_DAY_OF_PUBLICATION).monthOfPublication(UPDATED_MONTH_OF_PUBLICATION);

        restEditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEdition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEdition))
            )
            .andExpect(status().isOk());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeUpdate);
        Edition testEdition = editionList.get(editionList.size() - 1);
        assertThat(testEdition.getDayOfPublication()).isEqualTo(UPDATED_DAY_OF_PUBLICATION);
        assertThat(testEdition.getMonthOfPublication()).isEqualTo(UPDATED_MONTH_OF_PUBLICATION);
    }

    @Test
    @Transactional
    void fullUpdateEditionWithPatch() throws Exception {
        // Initialize the database
        editionRepository.saveAndFlush(edition);

        int databaseSizeBeforeUpdate = editionRepository.findAll().size();

        // Update the edition using partial update
        Edition partialUpdatedEdition = new Edition();
        partialUpdatedEdition.setId(edition.getId());

        partialUpdatedEdition.dayOfPublication(UPDATED_DAY_OF_PUBLICATION).monthOfPublication(UPDATED_MONTH_OF_PUBLICATION);

        restEditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEdition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEdition))
            )
            .andExpect(status().isOk());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeUpdate);
        Edition testEdition = editionList.get(editionList.size() - 1);
        assertThat(testEdition.getDayOfPublication()).isEqualTo(UPDATED_DAY_OF_PUBLICATION);
        assertThat(testEdition.getMonthOfPublication()).isEqualTo(UPDATED_MONTH_OF_PUBLICATION);
    }

    @Test
    @Transactional
    void patchNonExistingEdition() throws Exception {
        int databaseSizeBeforeUpdate = editionRepository.findAll().size();
        edition.setId(longCount.incrementAndGet());

        // Create the Edition
        EditionDTO editionDTO = editionMapper.toDto(edition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, editionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(editionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEdition() throws Exception {
        int databaseSizeBeforeUpdate = editionRepository.findAll().size();
        edition.setId(longCount.incrementAndGet());

        // Create the Edition
        EditionDTO editionDTO = editionMapper.toDto(edition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(editionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEdition() throws Exception {
        int databaseSizeBeforeUpdate = editionRepository.findAll().size();
        edition.setId(longCount.incrementAndGet());

        // Create the Edition
        EditionDTO editionDTO = editionMapper.toDto(edition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(editionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Edition in the database
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEdition() throws Exception {
        // Initialize the database
        editionRepository.saveAndFlush(edition);

        int databaseSizeBeforeDelete = editionRepository.findAll().size();

        // Delete the edition
        restEditionMockMvc
            .perform(delete(ENTITY_API_URL_ID, edition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Edition> editionList = editionRepository.findAll();
        assertThat(editionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
