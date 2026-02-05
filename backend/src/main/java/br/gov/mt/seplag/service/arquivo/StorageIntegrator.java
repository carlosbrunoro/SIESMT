package br.gov.mt.seplag.service.arquivo;

import org.springframework.web.multipart.MultipartFile;

public interface StorageIntegrator {

    String upload(final MultipartFile file);

    byte[] download(final String key);

    void delete(final String key);

    String gerarLinkDownloadPreAssinado(final String key);

}

