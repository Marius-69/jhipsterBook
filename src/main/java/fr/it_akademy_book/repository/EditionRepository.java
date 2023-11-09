package fr.it_akademy_book.repository;

import fr.it_akademy_book.domain.Edition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Edition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EditionRepository extends JpaRepository<Edition, Long> {}
