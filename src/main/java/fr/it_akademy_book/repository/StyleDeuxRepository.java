package fr.it_akademy_book.repository;

import fr.it_akademy_book.domain.StyleDeux;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StyleDeux entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StyleDeuxRepository extends JpaRepository<StyleDeux, Long> {}
