package br.gov.mt.seplag.infrastructure.integracao.regional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
    name = "regional",
    url = "${application.integrations.argus.endpoint}"
)
public interface RegionalFeignClient {

    @GetMapping("/v1/regionais")
    List<RegionalIntegracaoResponse> buscarRegionais();

}
