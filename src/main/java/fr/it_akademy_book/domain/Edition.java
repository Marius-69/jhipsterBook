package fr.it_akademy_book.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Edition.
 */
@Entity
@Table(name = "edition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Edition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "day_of_publication")
    private Long dayOfPublication;

    @Column(name = "month_of_publication")
    private String monthOfPublication;

    @JsonIgnoreProperties(value = { "author", "edition", "styles" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "edition")
    private Book book;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Edition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDayOfPublication() {
        return this.dayOfPublication;
    }

    public Edition dayOfPublication(Long dayOfPublication) {
        this.setDayOfPublication(dayOfPublication);
        return this;
    }

    public void setDayOfPublication(Long dayOfPublication) {
        this.dayOfPublication = dayOfPublication;
    }

    public String getMonthOfPublication() {
        return this.monthOfPublication;
    }

    public Edition monthOfPublication(String monthOfPublication) {
        this.setMonthOfPublication(monthOfPublication);
        return this;
    }

    public void setMonthOfPublication(String monthOfPublication) {
        this.monthOfPublication = monthOfPublication;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        if (this.book != null) {
            this.book.setEdition(null);
        }
        if (book != null) {
            book.setEdition(this);
        }
        this.book = book;
    }

    public Edition book(Book book) {
        this.setBook(book);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edition)) {
            return false;
        }
        return getId() != null && getId().equals(((Edition) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Edition{" +
            "id=" + getId() +
            ", dayOfPublication=" + getDayOfPublication() +
            ", monthOfPublication='" + getMonthOfPublication() + "'" +
            "}";
    }
}
