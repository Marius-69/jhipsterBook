package fr.it_akademy_book.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.it_akademy_book.domain.Style} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StyleDTO implements Serializable {

    private Long id;

    private String styleOfText;

    private BookDTO book;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStyleOfText() {
        return styleOfText;
    }

    public void setStyleOfText(String styleOfText) {
        this.styleOfText = styleOfText;
    }

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StyleDTO)) {
            return false;
        }

        StyleDTO styleDTO = (StyleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, styleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StyleDTO{" +
            "id=" + getId() +
            ", styleOfText='" + getStyleOfText() + "'" +
            ", book=" + getBook() +
            "}";
    }
}
