package fr.it_akademy_book.service.mapper;

import fr.it_akademy_book.domain.Edition;
import fr.it_akademy_book.service.dto.EditionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Edition} and its DTO {@link EditionDTO}.
 */
@Mapper(componentModel = "spring")
public interface EditionMapper extends EntityMapper<EditionDTO, Edition> {}
