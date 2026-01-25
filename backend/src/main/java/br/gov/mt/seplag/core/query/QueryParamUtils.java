package br.gov.mt.seplag.core.query;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class QueryParamUtils {

    private QueryParamUtils() {
    }

    public static String likeContainsIgnoreCase(final String value) {
        if (isBlank(value)) {
            return null;
        }

        return "%" + value.trim().toLowerCase() + "%";
    }

}
