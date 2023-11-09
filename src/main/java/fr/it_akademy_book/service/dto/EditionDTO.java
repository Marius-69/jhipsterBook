package fr.it_akademy_book.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.it_akademy_book.domain.Edition} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EditionDTO implements Serializable {

    private Long id;

    private Long dayOfPublication;

    private String monthOfPublication;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDayOfPublication() {
        return dayOfPublication;
    }

    public void setDayOfPublication(Long dayOfPublication) {
        this.dayOfPublication = dayOfPublication;
    }

    public String getMonthOfPublication() {
        return monthOfPublication;
    }

    public void setMonthOfPublication(String monthOfPublication) {
        this.monthOfPublication = monthOfPublication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EditionDTO)) {
            return false;
        }

        EditionDTO editionDTO = (EditionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, editionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EditionDTO{" +
            "id=" + getId() +
            ", dayOfPublication=" + getDayOfPublication() +
            ", monthOfPublication='" + getMonthOfPublication() + "'" +
            "}";
    }
}
