package fr.it_akademy_book.domain;

import static fr.it_akademy_book.domain.BookTestSamples.*;
import static fr.it_akademy_book.domain.StyleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.it_akademy_book.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StyleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Style.class);
        Style style1 = getStyleSample1();
        Style style2 = new Style();
        assertThat(style1).isNotEqualTo(style2);

        style2.setId(style1.getId());
        assertThat(style1).isEqualTo(style2);

        style2 = getStyleSample2();
        assertThat(style1).isNotEqualTo(style2);
    }

    @Test
    void bookTest() throws Exception {
        Style style = getStyleRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        style.setBook(bookBack);
        assertThat(style.getBook()).isEqualTo(bookBack);

        style.book(null);
        assertThat(style.getBook()).isNull();
    }
}
