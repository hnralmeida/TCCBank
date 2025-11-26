package bank.pagadorPSP.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BancoCentralServie {
    private final WebClient webClient;

    public BancoCentralServie(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getData() {
        return webClient.get()
                .uri("/data")
                .retrieve()
                .bodyToMono(String.class);
    }
}
