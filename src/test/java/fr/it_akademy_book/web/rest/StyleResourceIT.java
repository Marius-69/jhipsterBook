package fr.it_akademy_book.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.it_akademy_book.IntegrationTest;
import fr.it_akademy_book.domain.Style;
import fr.it_akademy_book.repository.StyleRepository;
import fr.it_akademy_book.service.dto.StyleDTO;
import fr.it_akademy_book.service.mapper.StyleMapper;
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
 * Integration tests for the {@link StyleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StyleResourceIT {

    private static final String DEFAULT_STYLE_OF_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_STYLE_OF_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/styles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StyleRepository styleRepository;

    @Autowired
    private StyleMapper styleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStyleMockMvc;

    private Style style;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Style createEntity(EntityManager em) {
        Style style = new Style().styleOfText(DEFAULT_STYLE_OF_TEXT);
        return style;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Style createUpdatedEntity(EntityManager em) {
        Style style = new Style().styleOfText(UPDATED_STYLE_OF_TEXT);
        return style;
    }

    @BeforeEach
    public void initTest() {
        style = createEntity(em);
    }

    @Test
    @Transactional
    void createStyle() throws Exception {
        int databaseSizeBeforeCreate = styleRepository.findAll().size();
        // Create the Style
        StyleDTO styleDTO = styleMapper.toDto(style);
        restStyleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(styleDTO)))
            .andExpect(status().isCreated());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeCreate + 1);
        Style testStyle = styleList.get(styleList.size() - 1);
        assertThat(testStyle.getStyleOfText()).isEqualTo(DEFAULT_STYLE_OF_TEXT);
    }

    @Test
    @Transactional
    void createStyleWithExistingId() throws Exception {
        // Create the Style with an existing ID
        style.setId(1L);
        StyleDTO styleDTO = styleMapper.toDto(style);

        int databaseSizeBeforeCreate = styleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStyleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(styleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStyles() throws Exception {
        // Initialize the database
        styleRepository.saveAndFlush(style);

        // Get all the styleList
        restStyleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(style.getId().intValue())))
            .andExpect(jsonPath("$.[*].styleOfText").value(hasItem(DEFAULT_STYLE_OF_TEXT)));
    }

    @Test
    @Transactional
    void getStyle() throws Exception {
        // Initialize the database
        styleRepository.saveAndFlush(style);

        // Get the style
        restStyleMockMvc
            .perform(get(ENTITY_API_URL_ID, style.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(style.getId().intValue()))
            .andExpect(jsonPath("$.styleOfText").value(DEFAULT_STYLE_OF_TEXT));
    }

    @Test
    @Transactional
    void getNonExistingStyle() throws Exception {
        // Get the style
        restStyleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStyle() throws Exception {
        // Initialize the database
        styleRepository.saveAndFlush(style);

        int databaseSizeBeforeUpdate = styleRepository.findAll().size();

        // Update the style
        Style updatedStyle = styleRepository.findById(style.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStyle are not directly saved in db
        em.detach(updatedStyle);
        updatedStyle.styleOfText(UPDATED_STYLE_OF_TEXT);
        StyleDTO styleDTO = styleMapper.toDto(updatedStyle);

        restStyleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, styleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(styleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeUpdate);
        Style testStyle = styleList.get(styleList.size() - 1);
        assertThat(testStyle.getStyleOfText()).isEqualTo(UPDATED_STYLE_OF_TEXT);
    }

    @Test
    @Transactional
    void putNonExistingStyle() throws Exception {
        int databaseSizeBeforeUpdate = styleRepository.findAll().size();
        style.setId(longCount.incrementAndGet());

        // Create the Style
        StyleDTO styleDTO = styleMapper.toDto(style);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStyleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, styleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(styleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStyle() throws Exception {
        int databaseSizeBeforeUpdate = styleRepository.findAll().size();
        style.setId(longCount.incrementAndGet());

        // Create the Style
        StyleDTO styleDTO = styleMapper.toDto(style);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStyleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(styleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStyle() throws Exception {
        int databaseSizeBeforeUpdate = styleRepository.findAll().size();
        style.setId(longCount.incrementAndGet());

        // Create the Style
        StyleDTO styleDTO = styleMapper.toDto(style);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStyleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(styleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStyleWithPatch() throws Exception {
        // Initialize the database
        styleRepository.saveAndFlush(style);

        int databaseSizeBeforeUpdate = styleRepository.findAll().size();

        // Update the style using partial update
        Style partialUpdatedStyle = new Style();
        partialUpdatedStyle.setId(style.getId());

        partialUpdatedStyle.styleOfText(UPDATED_STYLE_OF_TEXT);

        restStyleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStyle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStyle))
            )
            .andExpect(status().isOk());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeUpdate);
        Style testStyle = styleList.get(styleList.size() - 1);
        assertThat(testStyle.getStyleOfText()).isEqualTo(UPDATED_STYLE_OF_TEXT);
    }

    @Test
    @Transactional
    void fullUpdateStyleWithPatch() throws Exception {
        // Initialize the database
        styleRepository.saveAndFlush(style);

        int databaseSizeBeforeUpdate = styleRepository.findAll().size();

        // Update the style using partial update
        Style partialUpdatedStyle = new Style();
        partialUpdatedStyle.setId(style.getId());

        partialUpdatedStyle.styleOfText(UPDATED_STYLE_OF_TEXT);

        restStyleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStyle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStyle))
            )
            .andExpect(status().isOk());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeUpdate);
        Style testStyle = styleList.get(styleList.size() - 1);
        assertThat(testStyle.getStyleOfText()).isEqualTo(UPDATED_STYLE_OF_TEXT);
    }

    @Test
    @Transactional
    void patchNonExistingStyle() throws Exception {
        int databaseSizeBeforeUpdate = styleRepository.findAll().size();
        style.setId(longCount.incrementAndGet());

        // Create the Style
        StyleDTO styleDTO = styleMapper.toDto(style);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStyleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, styleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(styleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStyle() throws Exception {
        int databaseSizeBeforeUpdate = styleRepository.findAll().size();
        style.setId(longCount.incrementAndGet());

        // Create the Style
        StyleDTO styleDTO = styleMapper.toDto(style);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStyleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(styleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStyle() throws Exception {
        int databaseSizeBeforeUpdate = styleRepository.findAll().size();
        style.setId(longCount.incrementAndGet());

        // Create the Style
        StyleDTO styleDTO = styleMapper.toDto(style);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStyleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(styleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Style in the database
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStyle() throws Exception {
        // Initialize the database
        styleRepository.saveAndFlush(style);

        int databaseSizeBeforeDelete = styleRepository.findAll().size();

        // Delete the style
        restStyleMockMvc
            .perform(delete(ENTITY_API_URL_ID, style.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Style> styleList = styleRepository.findAll();
        assertThat(styleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
