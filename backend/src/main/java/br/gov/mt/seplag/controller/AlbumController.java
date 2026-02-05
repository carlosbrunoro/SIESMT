package br.gov.mt.seplag.controller;

import br.gov.mt.seplag.common.pageable.PageableFactory;
import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.dto.album.AlbumRequest;
import br.gov.mt.seplag.dto.album.AlbumResponse;
import br.gov.mt.seplag.dto.base.PageResponse;
import br.gov.mt.seplag.entity.Album;
import br.gov.mt.seplag.mapper.AlbumMapper;
import br.gov.mt.seplag.service.album.AlbumService;
import br.gov.mt.seplag.service.arquivo.ArquivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/albuns")
public class AlbumController {

    private final AlbumService albumService;
    private final ArquivoService arquivoService;
    private final PageableFactory pageableFactory;
    private final AlbumMapper mapper;

    public AlbumController(final AlbumService albumService,
                           final ArquivoService arquivoService,
                           final PageableFactory pageableFactory,
                           final AlbumMapper mapper) {
        this.albumService = albumService;
        this.arquivoService = arquivoService;
        this.pageableFactory = pageableFactory;
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

    @Operation(
        summary = "Buscar álbuns com paginação e filtros",
        description = """
            Retorna uma lista paginada de álbuns, permitindo filtros opcionais por nome do artista
            e tipo de formação (cantores solo e/ou bandas).
            
            A paginação é controlada pelos parâmetros <b>page</b> e <b>size</b>, com limites
            aplicados no backend para garantir desempenho e estabilidade da API.
            
            <ul>
              <li><b>page</b>: índice da página (base 0). Valores negativos são ajustados para 0.</li>
              <li><b>size</b>: quantidade de registros por página. O valor máximo permitido é 50.</li>
              <li>Caso <b>page</b> ou <b>size</b> não sejam informados, serão utilizados os valores padrão.</li>
            </ul>
            
            O retorno inclui metadados de paginação juntamente com a lista de álbuns.
            """
    )
    @GetMapping
    public PageResponse<AlbumResponse> findAll(
        @Parameter(
            description = "Nome do artista para filtro parcial ou completo"
        )
        @RequestParam(required = false) final String artistaNome,

        @Parameter(
            description = "Indica se álbuns de cantores solo devem ser incluídos no resultado"
        )
        @RequestParam(required = false) final Boolean flagCantores,

        @Parameter(
            description = "Indica se álbuns de bandas devem ser incluídos no resultado"
        )
        @RequestParam(required = false) final Boolean flagBandas,

        @Parameter(
            description = "Direção da ordenação alfabética pelo nome do artista (asc ou desc)",
            example = "asc"
        )
        @RequestParam(required = false, defaultValue = "asc") final String order,

        @Parameter(
            description = "Número da página (inicia em 0)",
            example = "0"
        )
        @RequestParam(required = false) final Integer page,

        @Parameter(
            description = "Quantidade de registros por página (máximo permitido: 50)",
            example = "10"
        )
        @RequestParam(required = false) final Integer size) {

        final Pageable pageable = pageableFactory.criar(page, size, order, "artista.nome");

        final Page<Album> albuns = albumService.listarPor(artistaNome, flagCantores, flagBandas, pageable);
        return PageResponse.from(albuns, mapper::toResponse);
    }

    @PostMapping(
        value = "/{idAlbum}/capas",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
        summary = "Adicionar capas ao álbum",
        description = "Realiza o upload de uma ou mais imagens e associa as capas ao álbum informado."
    )
    public ResponseEntity<Void> adicionarCapasAlbum(@PathVariable final Long idAlbum,
                                                    @RequestPart("files") final MultipartFile[] files) throws DomainException {
        albumService.adicionarCapasAlbum(idAlbum, files);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/capas/{idImagem}/link-pre-assinado")
    @Operation(
        summary = "Gerar link pré-assinado para acesso à imagem",
        description = """
            Gera um link pré-assinado para acesso temporário a um arquivo armazenado no MinIO.
            
            O link gerado permite a recuperação direta do arquivo sem necessidade de autenticação
            adicional e possui tempo de expiração configurado para 30 minutos, conforme requisito
            de segurança e controle de acesso.
            
            Após o prazo de expiração, o link torna-se inválido automaticamente.
            """
    )
    public ResponseEntity<String> gerarLinkDownload(@PathVariable final Long idImagem) {
        final String url = arquivoService.gerarLinkPreAssinado(idImagem);
        return ResponseEntity.ok(url);
    }

}