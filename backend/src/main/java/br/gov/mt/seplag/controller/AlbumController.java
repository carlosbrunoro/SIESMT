package br.gov.mt.seplag.controller;

import br.gov.mt.seplag.dto.album.AlbumRequest;
import br.gov.mt.seplag.dto.album.AlbumResponse;
import br.gov.mt.seplag.dto.base.PageResponse;
import br.gov.mt.seplag.entity.Album;
import br.gov.mt.seplag.mapper.AlbumMapper;
import br.gov.mt.seplag.service.album.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/albuns")
public class AlbumController {

    private final AlbumService albumService;
    private final AlbumMapper mapper;

    public AlbumController(final AlbumService albumService, final AlbumMapper mapper) {
        this.albumService = albumService;
        this.mapper = mapper;
    }

    @Operation(summary = "Criar álbum")
    @PostMapping
    public ResponseEntity<AlbumResponse> create(@RequestBody @Valid final AlbumRequest request) {
        final Album album = albumService.create(mapper.fromRequest(request));

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(mapper.toResponse(album));
    }

    @Operation(summary = "Atualizar álbum")
    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponse> update(@PathVariable final Long id,
                                                @RequestBody @Valid final AlbumRequest request) {
        final Album album = albumService.update(id, mapper.fromRequest(request));

        return ResponseEntity.ok(mapper.toResponse(album));
    }

    @Operation(summary = "Buscar álbum por id")
    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponse> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(mapper.toResponse(albumService.buscarPorId(id)));
    }

    @Operation(summary = "Buscar álbuns com paginação e filtros")
    @GetMapping
    public PageResponse<AlbumResponse> findAll(@RequestParam(required = false) final String artistaNome,
                                               @RequestParam(required = false) final Boolean flagCantores,
                                               @RequestParam(required = false) final Boolean flagBandas,
                                               final Pageable pageable) {
        final Page<Album> page = albumService.listarPor(artistaNome, flagCantores, flagBandas, pageable);
        return PageResponse.from(page, mapper::toResponse);
    }

}