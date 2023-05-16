package com.maersk.wiremock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EnergyApi {

    private final WebClient webClient;

    public EnergyApi(@Value("${energy.base-url}") String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public Mono<EnergyData> fecthEnergyData() {
        return webClient.get().uri("/dataset/Elspotprices")
                .retrieve()
                .bodyToMono(EnergyData.class)
                .onErrorContinue((t, o) -> new EnergyData());
    }
}
