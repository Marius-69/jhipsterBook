package fr.it_akademy_book.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StyleDeux.
 */
@Entity
@Table(name = "style_deux")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StyleDeux implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "style_of_text_deux")
    private String styleOfTextDeux;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StyleDeux id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStyleOfTextDeux() {
        return this.styleOfTextDeux;
    }

    public StyleDeux styleOfTextDeux(String styleOfTextDeux) {
        this.setStyleOfTextDeux(styleOfTextDeux);
        return this;
    }

    public void setStyleOfTextDeux(String styleOfTextDeux) {
        this.styleOfTextDeux = styleOfTextDeux;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StyleDeux)) {
            return false;
        }
        return getId() != null && getId().equals(((StyleDeux) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StyleDeux{" +
            "id=" + getId() +
            ", styleOfTextDeux='" + getStyleOfTextDeux() + "'" +
            "}";
    }
}
