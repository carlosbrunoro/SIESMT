package br.gov.mt.seplag;

import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.entity.Album;
import br.gov.mt.seplag.entity.Artista;
import br.gov.mt.seplag.event.AlbumCriadoEvent;
import br.gov.mt.seplag.repository.AlbumRepository;
import br.gov.mt.seplag.service.album.AlbumService;
import br.gov.mt.seplag.service.artista.ArtistaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @InjectMocks
    private AlbumService albumService;

    @Mock
    private AlbumRepository repository;

    @Mock
    private ArtistaService artistaService;

    @Mock
    private ApplicationEventPublisher publisher;

    @Test
    void deveCriarAlbumComSucesso() {
        final Artista artista = new Artista();
        artista.setId(1L);
        artista.setNome("João Carreiro & Capataz");

        final Album album = new Album();
        album.setNome("Lado A / Lado B");
        album.setArtistas(Set.of(artista));

        final Album albumSalvo = new Album();
        albumSalvo.setId(10L);
        albumSalvo.setNome("Hybrid Theory");

        when(repository.existsBy(any(), any())).thenReturn(false);
        when(repository.save(album)).thenReturn(albumSalvo);
        doNothing().when(artistaService).existsById(1L);

        final Album result = albumService.create(album);

        assertEquals(10L, result.getId());
        verify(repository).save(album);
        verify(publisher).publishEvent(any(AlbumCriadoEvent.class));
    }

    @Test
    void deveAtualizarAlbumComSucesso() {
        final Long id = 1L;

        final Artista artista = new Artista();
        artista.setId(1L);
        artista.setNome("Angra");

        final Album albumExistente = new Album();
        albumExistente.setId(id);
        albumExistente.setNome("Angels Cr");
        albumExistente.setAnoLancamento(2000);

        final Album request = new Album();
        request.setNome("Angels Cry");
        request.setAnoLancamento(1993);
        request.setArtistas(Set.of(artista));

        when(repository.findById(id)).thenReturn(Optional.of(albumExistente));
        when(repository.save(any(Album.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Album resultado = albumService.update(id, request);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(request.getNome(), resultado.getNome());
        assertEquals(request.getAnoLancamento(), resultado.getAnoLancamento());
        assertEquals(1, resultado.getArtistas().size());

        verify(repository).findById(id);
        verify(repository).save(albumExistente);
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        final Album album = new Album();

        final DomainException ex = assertThrows(
            DomainException.class,
            () -> albumService.create(album)
        );

        assertEquals("validation.field.required", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoNomeJaExistir() {
        final Album album = new Album();
        album.setNome("Back in Black");

        when(repository.existsBy(any(), any())).thenReturn(true);

        final DomainException ex = assertThrows(
            DomainException.class,
            () -> albumService.create(album)
        );

        assertEquals("business.album.nome.exists", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deveBuscarAlbumPorId() {
        final Album album = new Album();
        album.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(album));

        final Album result = albumService.buscarPorId(1L);

        assertEquals(1L, result.getId());
    }


    @Test
    void deveValidarExistenciaDosArtistas() {
        final Artista artista = new Artista();
        artista.setId(99L);
        artista.setNome("Pink Floyd");

        final Album album = new Album();
        album.setNome("The Dark Side of the Moon");
        album.setArtistas(Set.of(artista));

        doThrow(DomainException.notFound(
            "validation.entity.not.found", "Artista", 99L
        )).when(artistaService).existsById(99L);

        assertThrows(
            DomainException.class,
            () -> albumService.create(album)
        );
    }

}
