package br.gov.mt.seplag;

import br.gov.mt.seplag.entity.Album;
import br.gov.mt.seplag.entity.Arquivo;
import br.gov.mt.seplag.entity.ImagemAlbum;
import br.gov.mt.seplag.repository.ImagemAlbumRepository;
import br.gov.mt.seplag.service.album.imagem.ImagemAlbumService;
import br.gov.mt.seplag.service.arquivo.ArquivoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImagemAlbumServiceTest {

    @InjectMocks
    private ImagemAlbumService service;

    @Mock
    private ImagemAlbumRepository repository;

    @Mock
    private ArquivoService arquivoService;

    @Test
    void deveSalvarCapasComSucesso() {
        final Album album = new Album();
        final MultipartFile file1 = mock(MultipartFile.class);
        final MultipartFile file2 = mock(MultipartFile.class);
        final MultipartFile[] files = {file1, file2};

        final Arquivo arquivo = new Arquivo();

        when(arquivoService.uploadArquivo(any(MultipartFile.class))).thenReturn(arquivo);

        service.salvarCapas(album, files);

        verify(arquivoService, times(2))
            .uploadArquivo(any(MultipartFile.class));

        verify(repository, times(2))
            .save(any(ImagemAlbum.class));

        verify(arquivoService, never())
            .deletarArquivo(any());
    }

    @Test
    void deveDeletarArquivoQuandoErroAoSalvarImagem() {
        final Album album = new Album();
        final MultipartFile file = mock(MultipartFile.class);
        final MultipartFile[] files = {file};

        final Arquivo arquivo = new Arquivo();

        when(arquivoService.uploadArquivo(any(MultipartFile.class)))
            .thenReturn(arquivo);

        when(repository.save(any(ImagemAlbum.class)))
            .thenThrow(new RuntimeException("Erro ao salvar imagem"));

        assertThrows(RuntimeException.class,
            () -> service.salvarCapas(album, files));

        verify(arquivoService)
            .deletarArquivo(arquivo);

        verify(repository)
            .save(any(ImagemAlbum.class));
    }

}
