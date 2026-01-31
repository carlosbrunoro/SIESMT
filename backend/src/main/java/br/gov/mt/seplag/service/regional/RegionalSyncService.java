package br.gov.mt.seplag.service.regional;

import br.gov.mt.seplag.entity.Regional;
import br.gov.mt.seplag.infrastructure.integracao.regional.RegionalFeignClient;
import br.gov.mt.seplag.infrastructure.integracao.regional.RegionalIntegracaoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Service
public class RegionalSyncService {

    private final RegionalService regionalService;
    private final RegionalFeignClient regionalFeignClient;

    public RegionalSyncService(final RegionalService regionalService,
                               final RegionalFeignClient regionalFeignClient) {
        this.regionalService = regionalService;
        this.regionalFeignClient = regionalFeignClient;
    }

    @Transactional
    public void sincronizarRegionais() {
        final List<RegionalIntegracaoResponse> regionaisRemota = regionalFeignClient.buscarRegionais();
        final List<Regional> regionais = regionalService.findAllByAtivoTrue();

        if (isNotEmpty(regionaisRemota)) {
            final Map<Long, Regional> mapRegionais = regionais
                .stream()
                .collect(Collectors.toMap(Regional::getRegionalId, Function.identity()));

            for (final RegionalIntegracaoResponse remota : regionaisRemota) {
                final Regional regional = mapRegionais.get(remota.getId());

                if (isNull(regional)) {
                    final Regional novo = new Regional();
                    novo.setRegionalId(remota.getId());
                    novo.setNome(remota.getNome());
                    novo.setAtivo(TRUE);

                    regionalService.save(novo);
                    continue;
                }

                final boolean nomeAlterado = isFalse(Objects.equals(regional.getNome(), remota.getNome()));

                if (nomeAlterado) {
                    regional.setAtivo(FALSE);
                    regionalService.save(regional);

                    final Regional novo = new Regional();
                    novo.setRegionalId(remota.getId());
                    novo.setNome(remota.getNome());
                    novo.setAtivo(TRUE);

                    regionalService.save(new Regional(remota.getId(), remota.getNome(), true));
                }
            }

            final Set<Long> idsRemotos = regionaisRemota
                .stream()
                .map(RegionalIntegracaoResponse::getId)
                .collect(Collectors.toSet());

            for (final Regional local : regionais) {
                final Boolean existeRemoto = idsRemotos.contains(local.getRegionalId());

                if (isFalse(existeRemoto) && isTrue(local.getAtivo())) {
                    local.setAtivo(FALSE);
                    regionalService.save(local);
                }
            }
        }
    }

}
