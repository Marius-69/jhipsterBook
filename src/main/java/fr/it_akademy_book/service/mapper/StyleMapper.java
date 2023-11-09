package fr.it_akademy_book.service.mapper;

import fr.it_akademy_book.domain.Book;
import fr.it_akademy_book.domain.Style;
import fr.it_akademy_book.service.dto.BookDTO;
import fr.it_akademy_book.service.dto.StyleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Style} and its DTO {@link StyleDTO}.
 */
@Mapper(componentModel = "spring")
public interface StyleMapper extends EntityMapper<StyleDTO, Style> {
    @Mapping(target = "book", source = "book", qualifiedByName = "bookId")
    StyleDTO toDto(Style s);

    @Named("bookId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BookDTO toDtoBookId(Book book);
}
