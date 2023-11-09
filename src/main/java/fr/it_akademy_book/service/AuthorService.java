package fr.it_akademy_book.service;

import fr.it_akademy_book.service.dto.AuthorDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.it_akademy_book.domain.Author}.
 */
public interface AuthorService {
    /**
     * Save a author.
     *
     * @param authorDTO the entity to save.
     * @return the persisted entity.
     */
    AuthorDTO save(AuthorDTO authorDTO);

    /**
     * Updates a author.
     *
     * @param authorDTO the entity to update.
     * @return the persisted entity.
     */
    AuthorDTO update(AuthorDTO authorDTO);

    /**
     * Partially updates a author.
     *
     * @param authorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AuthorDTO> partialUpdate(AuthorDTO authorDTO);

    /**
     * Get all the authors.
     *
     * @return the list of entities.
     */
    List<AuthorDTO> findAll();

    /**
     * Get all the AuthorDTO where Book is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<AuthorDTO> findAllWhereBookIsNull();

    /**
     * Get the "id" author.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AuthorDTO> findOne(Long id);

    /**
     * Delete the "id" author.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
