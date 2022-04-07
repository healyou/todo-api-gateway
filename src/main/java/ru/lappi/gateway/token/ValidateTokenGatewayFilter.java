package ru.lappi.gateway.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.lappi.gateway.configuration.CircuitBreakerConfiguration;
import ru.lappi.gateway.token.resttempate.ValidateTokenRestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Nikita Gorodilov
 */
@Component
public class ValidateTokenGatewayFilter implements GatewayFilter {
    @Autowired
    private final ValidateTokenRestTemplate restTemplate;
    @Autowired
    private final ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory;

    public ValidateTokenGatewayFilter(
            ValidateTokenRestTemplate restTemplate,
            ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory
    ) {
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.defer(() -> {
            String token = getToken(exchange);

            /* Получаем существующий экземпляр circuit breaker */
            ReactiveCircuitBreaker reactiveCircuitBreaker = circuitBreakerFactory.create(
                    CircuitBreakerConfiguration.AUTH_API_CIRCUIT_BREAKER_NAME
            );
            /* Выполняем ассинхронный запрос */
            Mono<ResponseEntity<String>> validateTokenMono = reactiveCircuitBreaker.run(
                    Mono.defer(() -> Mono.just(restTemplate.validateToken(token))),
                    Mono::error
            );
            /* Если не было ошибок - обрабатываем результат */
            return validateTokenMono.flatMap((Function<ResponseEntity<String>, Mono<Void>>) response -> {
                if (response == null) {
                    return Mono.error(new RuntimeException("Непредвиденная ошибка при проверки токена"));
                } else if (response.getStatusCode() == HttpStatus.OK) {
                    return chain.filter(exchange);
                } else {
                    return Mono.error(new ValidateTokenException(response.getStatusCode(), response.getBody()));
                }
            });
        });
    }

    private String getToken(ServerWebExchange exchange) {
        List<String> tokens = exchange.getRequest().getHeaders().get("X-Access-Token");
        return Optional.ofNullable(tokens).flatMap(t -> t.stream().findFirst()).orElse(null);
    }
}
