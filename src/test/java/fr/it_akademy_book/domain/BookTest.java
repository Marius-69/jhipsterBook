package fr.it_akademy_book.domain;

import static fr.it_akademy_book.domain.AuthorTestSamples.*;
import static fr.it_akademy_book.domain.BookTestSamples.*;
import static fr.it_akademy_book.domain.EditionTestSamples.*;
import static fr.it_akademy_book.domain.StyleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.it_akademy_book.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Book.class);
        Book book1 = getBookSample1();
        Book book2 = new Book();
        assertThat(book1).isNotEqualTo(book2);

        book2.setId(book1.getId());
        assertThat(book1).isEqualTo(book2);

        book2 = getBookSample2();
        assertThat(book1).isNotEqualTo(book2);
    }

    @Test
    void authorTest() throws Exception {
        Book book = getBookRandomSampleGenerator();
        Author authorBack = getAuthorRandomSampleGenerator();

        book.setAuthor(authorBack);
        assertThat(book.getAuthor()).isEqualTo(authorBack);

        book.author(null);
        assertThat(book.getAuthor()).isNull();
    }

    @Test
    void editionTest() throws Exception {
        Book book = getBookRandomSampleGenerator();
        Edition editionBack = getEditionRandomSampleGenerator();

        book.setEdition(editionBack);
        assertThat(book.getEdition()).isEqualTo(editionBack);

        book.edition(null);
        assertThat(book.getEdition()).isNull();
    }

    @Test
    void styleTest() throws Exception {
        Book book = getBookRandomSampleGenerator();
        Style styleBack = getStyleRandomSampleGenerator();

        book.addStyle(styleBack);
        assertThat(book.getStyles()).containsOnly(styleBack);
        assertThat(styleBack.getBook()).isEqualTo(book);

        book.removeStyle(styleBack);
        assertThat(book.getStyles()).doesNotContain(styleBack);
        assertThat(styleBack.getBook()).isNull();

        book.styles(new HashSet<>(Set.of(styleBack)));
        assertThat(book.getStyles()).containsOnly(styleBack);
        assertThat(styleBack.getBook()).isEqualTo(book);

        book.setStyles(new HashSet<>());
        assertThat(book.getStyles()).doesNotContain(styleBack);
        assertThat(styleBack.getBook()).isNull();
    }
}
