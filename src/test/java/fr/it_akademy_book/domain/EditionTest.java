package fr.it_akademy_book.domain;

import static fr.it_akademy_book.domain.BookTestSamples.*;
import static fr.it_akademy_book.domain.EditionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.it_akademy_book.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EditionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Edition.class);
        Edition edition1 = getEditionSample1();
        Edition edition2 = new Edition();
        assertThat(edition1).isNotEqualTo(edition2);

        edition2.setId(edition1.getId());
        assertThat(edition1).isEqualTo(edition2);

        edition2 = getEditionSample2();
        assertThat(edition1).isNotEqualTo(edition2);
    }

    @Test
    void bookTest() throws Exception {
        Edition edition = getEditionRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        edition.setBook(bookBack);
        assertThat(edition.getBook()).isEqualTo(bookBack);
        assertThat(bookBack.getEdition()).isEqualTo(edition);

        edition.book(null);
        assertThat(edition.getBook()).isNull();
        assertThat(bookBack.getEdition()).isNull();
    }
}
