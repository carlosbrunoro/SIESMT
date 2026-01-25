package br.gov.mt.seplag.entity;

import br.gov.mt.seplag.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "album")
public class Album extends BaseEntity {

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "ano_lancamento", nullable = false)
    private Integer anoLancamento;

    @ManyToMany
    @JoinTable(
        name = "artista_album",
        joinColumns = @JoinColumn(name = "album_id"),
        inverseJoinColumns = @JoinColumn(name = "artista_id")
    )
    private Set<Artista> artistas;

}