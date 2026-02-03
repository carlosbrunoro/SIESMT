package br.gov.mt.seplag;

import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.entity.Artista;
import br.gov.mt.seplag.repository.ArtistaRepository;
import br.gov.mt.seplag.service.artista.ArtistaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistaServiceTest {

    @InjectMocks
    private ArtistaService service;

    @Mock
    private ArtistaRepository repository;

    @Test
    void deveRetornarArtistaQuandoExiste() {
        final Artista artista = Artista.builder().id(1L).nome("Nome").build();
        when(repository.findById(1L)).thenReturn(Optional.of(artista));

        final Artista result = service.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void deveLancarExcecaoQuandoArtistaNaoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        final DomainException exception = assertThrows(DomainException.class,
            () -> service.buscarPorId(1L));

        assertTrue(exception.getMessage().contains("validation.entity.not.found"));
    }
}