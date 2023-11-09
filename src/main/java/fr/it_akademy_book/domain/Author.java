package fr.it_akademy_book.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Author.
 */
@Entity
@Table(name = "author")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name_author")
    private String nameAuthor;

    @Column(name = "surname_author")
    private String surnameAuthor;

    @JsonIgnoreProperties(value = { "author", "edition", "styles" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "author")
    private Book book;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Author id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameAuthor() {
        return this.nameAuthor;
    }

    public Author nameAuthor(String nameAuthor) {
        this.setNameAuthor(nameAuthor);
        return this;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
    }

    public String getSurnameAuthor() {
        return this.surnameAuthor;
    }

    public Author surnameAuthor(String surnameAuthor) {
        this.setSurnameAuthor(surnameAuthor);
        return this;
    }

    public void setSurnameAuthor(String surnameAuthor) {
        this.surnameAuthor = surnameAuthor;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        if (this.book != null) {
            this.book.setAuthor(null);
        }
        if (book != null) {
            book.setAuthor(this);
        }
        this.book = book;
    }

    public Author book(Book book) {
        this.setBook(book);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Author)) {
            return false;
        }
        return getId() != null && getId().equals(((Author) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Author{" +
            "id=" + getId() +
            ", nameAuthor='" + getNameAuthor() + "'" +
            ", surnameAuthor='" + getSurnameAuthor() + "'" +
            "}";
    }
}
