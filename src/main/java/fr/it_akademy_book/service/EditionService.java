package fr.it_akademy_book.service;

import fr.it_akademy_book.service.dto.EditionDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.it_akademy_book.domain.Edition}.
 */
public interface EditionService {
    /**
     * Save a edition.
     *
     * @param editionDTO the entity to save.
     * @return the persisted entity.
     */
    EditionDTO save(EditionDTO editionDTO);

    /**
     * Updates a edition.
     *
     * @param editionDTO the entity to update.
     * @return the persisted entity.
     */
    EditionDTO update(EditionDTO editionDTO);

    /**
     * Partially updates a edition.
     *
     * @param editionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EditionDTO> partialUpdate(EditionDTO editionDTO);

    /**
     * Get all the editions.
     *
     * @return the list of entities.
     */
    List<EditionDTO> findAll();

    /**
     * Get all the EditionDTO where Book is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<EditionDTO> findAllWhereBookIsNull();

    /**
     * Get the "id" edition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EditionDTO> findOne(Long id);

    /**
     * Delete the "id" edition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
