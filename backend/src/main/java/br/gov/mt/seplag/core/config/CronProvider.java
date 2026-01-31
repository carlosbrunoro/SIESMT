package br.gov.mt.seplag.core.config;

import br.gov.mt.seplag.core.config.properties.ApplicationProperties;
import org.springframework.stereotype.Component;

@Component("cronProvider")
public class CronProvider {

    private final ApplicationProperties applicationProperties;

    public CronProvider(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getCronRegionalSync() {
        return applicationProperties.getScheduler().getRegionalSync().getCron();
    }

}
