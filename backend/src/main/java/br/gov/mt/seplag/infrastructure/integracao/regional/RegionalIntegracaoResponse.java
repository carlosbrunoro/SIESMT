package br.gov.mt.seplag.infrastructure.integracao.regional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionalIntegracaoResponse {

    private Long id;
    private String nome;
    private Boolean ativo;

}
