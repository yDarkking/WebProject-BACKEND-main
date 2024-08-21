package com.example.user.apiconsume;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.user.models.User;

import reactor.core.publisher.Mono;

@Service
public class SpringClient {

    private final WebClient webClient;
    private static final Logger log = LoggerFactory.getLogger(SpringClient.class);

    @Autowired
    public SpringClient(WebClient.Builder webClientBuilder, @Value("${api.base.url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<List<User>> getAllUsers() {
        return webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(User.class)
                .collectList()
                .timeout(Duration.ofSeconds(10)) // configuração de timeout
                .doOnError(ex -> {
                    // tratamento de erros
                    log.error("Erro ao buscar usuários", ex);
                    throw new RuntimeException("Erro ao buscar usuários", ex);
                })
                .doOnNext(users -> {
                    // logging
                    log.info("Usuários encontrados: {}", users);
                });
    }
    public Mono<Void> deleteUser(int userId) {
        return webClient.delete()
                .uri("/user/{id}", userId)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofSeconds(10)) // configuração de timeout
                .doOnError(ex -> {
                    // tratamento de erros
                    log.error("Erro ao deletar usuário", ex);
                    throw new RuntimeException("Erro ao deletar usuário", ex);
                })
                .doOnNext(voidResponse -> {
                    // logging
                    log.info("Usuário deletado com sucesso");
                });
    }
}