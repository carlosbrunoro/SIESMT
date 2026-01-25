package br.gov.mt.seplag.entity;

import br.gov.mt.seplag.entity.base.BaseEntity;
import br.gov.mt.seplag.enumeration.TipoArtista;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "artista")
public class Artista extends BaseEntity {

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 100)
    private TipoArtista tipo;

}