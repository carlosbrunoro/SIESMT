package br.gov.mt.seplag.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (isNull(o) || getClass() != o.getClass()) {
            return false;
        }

        final BaseEntity that = (BaseEntity) o;

        return Objects.nonNull(id) && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return nonNull(id) ? id.hashCode() : 0;
    }

}