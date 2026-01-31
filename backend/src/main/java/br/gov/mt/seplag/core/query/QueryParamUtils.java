package br.gov.mt.seplag.core.query;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class QueryParamUtils {

    private QueryParamUtils() {
    }

    /**
     * Retorna uma string formatada para SQL LIKE, ignorando maiúsculas/minúsculas.
     * Retorna {@code null} se a entrada for nula ou vazia, sinalizando que o filtro deve ser ignorado.
     *
     * <p>Exemplos:
     * <pre>
     * likeContainsIgnoreCase(" Carlos ") -> "%carlos%"
     * likeContainsIgnoreCase("") -> null
     * likeContainsIgnoreCase(null) -> null
     * </pre>
     *
     * @param value a string de entrada
     * @return a string formatada para SQL LIKE ou {@code null} se vazia
     */
    public static String likeContainsIgnoreCase(final String value) {
        if (isBlank(value)) {
            return null;
        }

        return "%" + value.trim().toLowerCase() + "%";
    }

}
