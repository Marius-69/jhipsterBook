package fr.it_akademy_book.service.mapper;

import fr.it_akademy_book.domain.Author;
import fr.it_akademy_book.service.dto.AuthorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {}
