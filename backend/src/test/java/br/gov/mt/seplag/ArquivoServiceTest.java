package br.gov.mt.seplag;

import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.repository.ArquivoRepository;
import br.gov.mt.seplag.service.arquivo.ArquivoService;
import br.gov.mt.seplag.service.arquivo.StorageIntegrator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArquivoServiceTest {

    @InjectMocks
    private ArquivoService service;

    @Mock
    private StorageIntegrator storageIntegrator;

    @Mock
    private ArquivoRepository arquivoRepository;


    @Test
    void deveLancarExcecaoQuandoArquivoForNulo() {
        assertThrows(DomainException.class, () -> service.uploadArquivo(null));

        verifyNoInteractions(storageIntegrator, arquivoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoArquivoForVazio() {
        final MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        assertThrows(DomainException.class, () -> service.uploadArquivo(file));

        verifyNoInteractions(storageIntegrator, arquivoRepository);
    }

}
