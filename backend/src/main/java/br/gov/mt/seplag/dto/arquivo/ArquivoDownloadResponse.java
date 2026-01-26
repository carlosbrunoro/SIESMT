package br.gov.mt.seplag.dto.arquivo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArquivoDownloadResponse {

    private byte[] dados;
    private String nomeOriginal;
    private String tipoMime;

}
