package br.gov.mt.seplag.controller;

import br.gov.mt.seplag.common.pageable.PageableFactory;
import br.gov.mt.seplag.dto.base.PageResponse;
import br.gov.mt.seplag.entity.Regional;
import br.gov.mt.seplag.infrastructure.integracao.regional.RegionalIntegracaoResponse;
import br.gov.mt.seplag.mapper.RegionalMapper;
import br.gov.mt.seplag.service.regional.RegionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/regionais")
public class RegionalController {

    private final RegionalService regionalService;
    private final PageableFactory pageableFactory;
    private final RegionalMapper mapper;

    public RegionalController(final RegionalService regionalService,
                              final PageableFactory pageableFactory,
                              final RegionalMapper mapper) {
        this.regionalService = regionalService;
        this.pageableFactory = pageableFactory;
        this.mapper = mapper;
    }

    @Operation(
        summary = "Buscar regional por id",
        description = "Retorna os dados de uma regional específica a partir do seu identificador."
    )
    @GetMapping("/{id}")
    public ResponseEntity<RegionalIntegracaoResponse> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(mapper.toResponse(regionalService.buscarPorId(id)));
    }

    @Operation(
        summary = "Buscar regionais com paginação e filtro por nome",
        description = """
            Retorna uma lista paginada de regionais.
            Permite filtrar pelo nome da regional e controlar paginação e ordenação
            por meio dos parâmetros padrão do Spring Pageable.
            """
    )
    @GetMapping
    public PageResponse<RegionalIntegracaoResponse> findAll(
        @Parameter(
            description = "Nome da regional para filtro parcial ou completo"
        )
        @RequestParam(required = false) final String nomeRegional,

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
        final Pageable pageable = pageableFactory.criar(page, size, order);

        final Page<Regional> regionais = regionalService.listarPor(nomeRegional, pageable);
        return PageResponse.from(regionais, mapper::toResponse);
    }

}