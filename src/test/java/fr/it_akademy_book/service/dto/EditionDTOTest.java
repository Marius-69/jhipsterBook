package fr.it_akademy_book.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.it_akademy_book.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EditionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EditionDTO.class);
        EditionDTO editionDTO1 = new EditionDTO();
        editionDTO1.setId(1L);
        EditionDTO editionDTO2 = new EditionDTO();
        assertThat(editionDTO1).isNotEqualTo(editionDTO2);
        editionDTO2.setId(editionDTO1.getId());
        assertThat(editionDTO1).isEqualTo(editionDTO2);
        editionDTO2.setId(2L);
        assertThat(editionDTO1).isNotEqualTo(editionDTO2);
        editionDTO1.setId(null);
        assertThat(editionDTO1).isNotEqualTo(editionDTO2);
    }
}
