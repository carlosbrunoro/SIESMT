package br.gov.mt.seplag.entity;

import br.gov.mt.seplag.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "regional")
public class Regional extends BaseEntity {

    public Regional(final Long regionalId, final String nome, final Boolean ativo) {
        this.regionalId = regionalId;
        this.nome = nome;
        this.ativo = ativo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "regional_id", nullable = false, unique = true)
    private Long regionalId;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "ativo")
    private Boolean ativo;


}