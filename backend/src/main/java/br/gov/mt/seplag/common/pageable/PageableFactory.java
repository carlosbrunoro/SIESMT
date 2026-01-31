package br.gov.mt.seplag.common.pageable;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class PageableFactory {

    private static final int SIZE_MAXIMO = 50;
    private static final int SIZE_PADRAO = 10;
    private static final String CAMPO_ORDENACAO_PADRAO = "id";

    public Pageable criar(final Integer page,
                          final Integer size,
                          final String order) {
        return criar(page, size, order, CAMPO_ORDENACAO_PADRAO);
    }

    public Pageable criar(final Integer page,
                          final Integer size,
                          final String order,
                          final String campoOrdenacao
    ) {
        final int pageSeguro = nonNull(page) ? Math.max(page, 0) : 0;
        final int sizeSeguro = nonNull(size)
            ? Math.min(size, SIZE_MAXIMO)
            : SIZE_PADRAO;

        final Sort.Direction direction = Sort.Direction.fromOptionalString(order)
            .orElse(Sort.Direction.ASC);

        final String campoSeguroOrdenacao = isNotBlank(campoOrdenacao)
            ? campoOrdenacao
            : CAMPO_ORDENACAO_PADRAO;

        return PageRequest.of(
            pageSeguro,
            sizeSeguro,
            Sort.by(direction, campoSeguroOrdenacao)
        );
    }

}

