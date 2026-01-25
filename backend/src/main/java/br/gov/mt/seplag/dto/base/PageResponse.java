package br.gov.mt.seplag.dto.base;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    private PageResponse() {
    }

    public static <T, R> PageResponse<R> from(final Page<T> page,
                                              final Function<T, R> mapper) {
        final Page<R> mappedPage = page.map(mapper);
        final PageResponse<R> response = new PageResponse<>();

        response.content = mappedPage.getContent();
        response.page = mappedPage.getNumber();
        response.size = mappedPage.getSize();
        response.totalElements = mappedPage.getTotalElements();
        response.totalPages = mappedPage.getTotalPages();
        response.first = mappedPage.isFirst();
        response.last = mappedPage.isLast();

        return response;
    }
}
