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
@Table(name = "arquivo")
public class Arquivo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "storage_key", nullable = false)
    private String storageKey;

    @Column(name = "nome_original")
    private String nomeOriginal;

    @Column(name = "tipo_mime", length = 100)
    private String tipoMime;

    @Column(name = "tamanho")
    private Long tamanho;

}
