package fr.it_akademy_book.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.it_akademy_book.domain.Author} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthorDTO implements Serializable {

    private Long id;

    private String nameAuthor;

    private String surnameAuthor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
    }

    public String getSurnameAuthor() {
        return surnameAuthor;
    }

    public void setSurnameAuthor(String surnameAuthor) {
        this.surnameAuthor = surnameAuthor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorDTO)) {
            return false;
        }

        AuthorDTO authorDTO = (AuthorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, authorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorDTO{" +
            "id=" + getId() +
            ", nameAuthor='" + getNameAuthor() + "'" +
            ", surnameAuthor='" + getSurnameAuthor() + "'" +
            "}";
    }
}
