package fr.it_akademy_book.domain;

import static fr.it_akademy_book.domain.StyleDeuxTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.it_akademy_book.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StyleDeuxTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StyleDeux.class);
        StyleDeux styleDeux1 = getStyleDeuxSample1();
        StyleDeux styleDeux2 = new StyleDeux();
        assertThat(styleDeux1).isNotEqualTo(styleDeux2);

        styleDeux2.setId(styleDeux1.getId());
        assertThat(styleDeux1).isEqualTo(styleDeux2);

        styleDeux2 = getStyleDeuxSample2();
        assertThat(styleDeux1).isNotEqualTo(styleDeux2);
    }
}
