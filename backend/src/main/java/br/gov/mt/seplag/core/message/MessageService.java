package br.gov.mt.seplag.core.message;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageService {

    private final ResourceBundleMessageSource messageSource;

    public MessageService(final ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String toLocale(final String msgCode) {
        final Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, null, locale);
    }

    public String toLocale(final String msgCode, final Object... args) {
        return messageSource.getMessage(msgCode, args, msgCode, LocaleContextHolder.getLocale());
    }

}
