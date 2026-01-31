package br.gov.mt.seplag.schedule;

import br.gov.mt.seplag.core.config.properties.ApplicationProperties;
import br.gov.mt.seplag.service.regional.RegionalSyncService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Component
public class RegionalSyncScheduler {
    private final RegionalSyncService regionalSyncService;
    private final ApplicationProperties applicationProperties;

    public RegionalSyncScheduler(final RegionalSyncService regionalSyncService,
                                 final ApplicationProperties applicationProperties) {
        this.regionalSyncService = regionalSyncService;
        this.applicationProperties = applicationProperties;
    }

    @Scheduled(cron = "#{@cronProvider.getCronRegionalSync()}")
    public void sincronizarRegionais() {
        if (isTrue(applicationProperties.getScheduler().getRegionalSync().getEnabled())) {
            regionalSyncService.sincronizarRegionais();
        }
    }

}
