package fr.it_akademy_book.service.mapper;

import fr.it_akademy_book.domain.Author;
import fr.it_akademy_book.domain.Book;
import fr.it_akademy_book.domain.Edition;
import fr.it_akademy_book.service.dto.AuthorDTO;
import fr.it_akademy_book.service.dto.BookDTO;
import fr.it_akademy_book.service.dto.EditionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Book} and its DTO {@link BookDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDTO, Book> {
    @Mapping(target = "author", source = "author", qualifiedByName = "authorId")
    @Mapping(target = "edition", source = "edition", qualifiedByName = "editionId")
    BookDTO toDto(Book s);

    @Named("authorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AuthorDTO toDtoAuthorId(Author author);

    @Named("editionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EditionDTO toDtoEditionId(Edition edition);
}
