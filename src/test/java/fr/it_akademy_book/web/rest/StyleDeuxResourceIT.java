package fr.it_akademy_book.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.it_akademy_book.IntegrationTest;
import fr.it_akademy_book.domain.StyleDeux;
import fr.it_akademy_book.repository.StyleDeuxRepository;
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
 * Integration tests for the {@link StyleDeuxResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StyleDeuxResourceIT {

    private static final String DEFAULT_STYLE_OF_TEXT_DEUX = "AAAAAAAAAA";
    private static final String UPDATED_STYLE_OF_TEXT_DEUX = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/style-deuxes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StyleDeuxRepository styleDeuxRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStyleDeuxMockMvc;

    private StyleDeux styleDeux;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StyleDeux createEntity(EntityManager em) {
        StyleDeux styleDeux = new StyleDeux().styleOfTextDeux(DEFAULT_STYLE_OF_TEXT_DEUX);
        return styleDeux;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StyleDeux createUpdatedEntity(EntityManager em) {
        StyleDeux styleDeux = new StyleDeux().styleOfTextDeux(UPDATED_STYLE_OF_TEXT_DEUX);
        return styleDeux;
    }

    @BeforeEach
    public void initTest() {
        styleDeux = createEntity(em);
    }

    @Test
    @Transactional
    void createStyleDeux() throws Exception {
        int databaseSizeBeforeCreate = styleDeuxRepository.findAll().size();
        // Create the StyleDeux
        restStyleDeuxMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(styleDeux)))
            .andExpect(status().isCreated());

        // Validate the StyleDeux in the database
        List<StyleDeux> styleDeuxList = styleDeuxRepository.findAll();
        assertThat(styleDeuxList).hasSize(databaseSizeBeforeCreate + 1);
        StyleDeux testStyleDeux = styleDeuxList.get(styleDeuxList.size() - 1);
        assertThat(testStyleDeux.getStyleOfTextDeux()).isEqualTo(DEFAULT_STYLE_OF_TEXT_DEUX);
    }

    @Test
    @Transactional
    void createStyleDeuxWithExistingId() throws Exception {
        // Create the StyleDeux with an existing ID
        styleDeux.setId(1L);

        int databaseSizeBeforeCreate = styleDeuxRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStyleDeuxMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(styleDeux)))
            .andExpect(status().isBadRequest());

        // Validate the StyleDeux in the database
        List<StyleDeux> styleDeuxList = styleDeuxRepository.findAll();
        assertThat(styleDeuxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStyleDeuxes() throws Exception {
        // Initialize the database
        styleDeuxRepository.saveAndFlush(styleDeux);

        // Get all the styleDeuxList
        restStyleDeuxMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(styleDeux.getId().intValue())))
            .andExpect(jsonPath("$.[*].styleOfTextDeux").value(hasItem(DEFAULT_STYLE_OF_TEXT_DEUX)));
    }

    @Test
    @Transactional
    void getStyleDeux() throws Exception {
        // Initialize the database
        styleDeuxRepository.saveAndFlush(styleDeux);

        // Get the styleDeux
        restStyleDeuxMockMvc
            .perform(get(ENTITY_API_URL_ID, styleDeux.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(styleDeux.getId().intValue()))
            .andExpect(jsonPath("$.styleOfTextDeux").value(DEFAULT_STYLE_OF_TEXT_DEUX));
    }

    @Test
    @Transactional
    void getNonExistingStyleDeux() throws Exception {
        // Get the styleDeux
        restStyleDeuxMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStyleDeux() throws Exception {
        // Initialize the database
        styleDeuxRepository.saveAndFlush(styleDeux);

        int databaseSizeBeforeUpdate = styleDeuxRepository.findAll().size();

        // Update the styleDeux
        StyleDeux updatedStyleDeux = styleDeuxRepository.findById(styleDeux.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStyleDeux are not directly saved in db
        em.detach(updatedStyleDeux);
        updatedStyleDeux.styleOfTextDeux(UPDATED_STYLE_OF_TEXT_DEUX);

        restStyleDeuxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStyleDeux.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStyleDeux))
            )
            .andExpect(status().isOk());

        // Validate the StyleDeux in the database
        List<StyleDeux> styleDeuxList = styleDeuxRepository.findAll();
        assertThat(styleDeuxList).hasSize(databaseSizeBeforeUpdate);
        StyleDeux testStyleDeux = styleDeuxList.get(styleDeuxList.size() - 1);
        assertThat(testStyleDeux.getStyleOfTextDeux()).isEqualTo(UPDATED_STYLE_OF_TEXT_DEUX);
    }

    @Test
    @Transactional
    void putNonExistingStyleDeux() throws Exception {
        int databaseSizeBeforeUpdate = styleDeuxRepository.findAll().size();
        styleDeux.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStyleDeuxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, styleDeux.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(styleDeux))
            )
            .andExpect(status().isBadRequest());

        // Validate the StyleDeux in the database
        List<StyleDeux> styleDeuxList = styleDeuxRepository.findAll();
        assertThat(styleDeuxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStyleDeux() throws Exception {
        int databaseSizeBeforeUpdate = styleDeuxRepository.findAll().size();
        styleDeux.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStyleDeuxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(styleDeux))
            )
            .andExpect(status().isBadRequest());

        // Validate the StyleDeux in the database
        List<StyleDeux> styleDeuxList = styleDeuxRepository.findAll();
        assertThat(styleDeuxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStyleDeux() throws Exception {
        int databaseSizeBeforeUpdate = styleDeuxRepository.findAll().size();
        styleDeux.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStyleDeuxMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(styleDeux)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StyleDeux in the database
        List<StyleDeux> styleDeuxList = styleDeuxRepository.findAll();
        assertThat(styleDeuxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStyleDeuxWithPatch() throws Exception {
        // Initialize the database
        styleDeuxRepository.saveAndFlush(styleDeux);

        int databaseSizeBeforeUpdate = styleDeuxRepository.findAll().size();

        // Update the styleDeux using partial update
        StyleDeux partialUpdatedStyleDeux = new StyleDeux();
        partialUpdatedStyleDeux.setId(styleDeux.getId());

        restStyleDeuxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStyleDeux.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStyleDeux))
            )
            .andExpect(status().isOk());

        // Validate the StyleDeux in the database
        List<StyleDeux> styleDeuxList = styleDeuxRepository.findAll();
        assertThat(styleDeuxList).hasSize(databaseSizeBeforeUpdate);
        StyleDeux testStyleDeux = styleDeuxList.get(styleDeuxList.size() - 1);
        assertThat(testStyleDeux.getStyleOfTextDeux()).isEqualTo(DEFAULT_STYLE_OF_TEXT_DEUX);
    }

    @Test
    @Transactional
    void fullUpdateStyleDeuxWithPatch() throws Exception {
        // Initialize the database
        styleDeuxRepository.saveAndFlush(styleDeux);

        int databaseSizeBeforeUpdate = styleDeuxRepository.findAll().size();

        // Update the styleDeux using partial update
        StyleDeux partialUpdatedStyleDeux = new StyleDeux();
        partialUpdatedStyleDeux.setId(styleDeux.getId());

        partialUpdatedStyleDeux.styleOfTextDeux(UPDATED_STYLE_OF_TEXT_DEUX);

        restStyleDeuxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStyleDeux.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStyleDeux))
            )
            .andExpect(status().isOk());

        // Validate the StyleDeux in the database
        List<StyleDeux> styleDeuxList = styleDeuxRepository.findAll();
        assertThat(styleDeuxList).hasSize(databaseSizeBeforeUpdate);
        StyleDeux testStyleDeux = styleDeuxList.get(styleDeuxList.size() - 1);
        assertThat(testStyleDeux.getStyleOfTextDeux()).isEqualTo(UPDATED_STYLE_OF_TEXT_DEUX);
    }

    @Test
    @Transactional
    void patchNonExistingStyleDeux() throws Exception {
        int databaseSizeBeforeUpdate = styleDeuxRepository.findAll().size();
        styleDeux.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStyleDeuxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, styleDeux.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(styleDeux))
            )
            .andExpect(status().isBadRequest());

        // Validate the StyleDeux in the database
        List<StyleDeux> styleDeuxList = styleDeuxRepository.findAll();
        assertThat(styleDeuxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStyleDeux() throws Exception {
        int databaseSizeBeforeUpdate = styleDeuxRepository.findAll().size();
        styleDeux.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStyleDeuxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(styleDeux))
            )
            .andExpect(status().isBadRequest());

        // Validate the StyleDeux in the database
        List<StyleDeux> styleDeuxList = styleDeuxRepository.findAll();
        assertThat(styleDeuxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStyleDeux() throws Exception {
        int databaseSizeBeforeUpdate = styleDeuxRepository.findAll().size();
        styleDeux.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStyleDeuxMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(styleDeux))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StyleDeux in the database
        List<StyleDeux> styleDeuxList = styleDeuxRepository.findAll();
        assertThat(styleDeuxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStyleDeux() throws Exception {
        // Initialize the database
        styleDeuxRepository.saveAndFlush(styleDeux);

        int databaseSizeBeforeDelete = styleDeuxRepository.findAll().size();

        // Delete the styleDeux
        restStyleDeuxMockMvc
            .perform(delete(ENTITY_API_URL_ID, styleDeux.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StyleDeux> styleDeuxList = styleDeuxRepository.findAll();
        assertThat(styleDeuxList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
