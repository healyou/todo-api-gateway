package ru.lappi.gateway.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.lappi.gateway.token.resttempate.ValidateTokenRestTemplate;

import java.util.List;
import java.util.Optional;

/**
 * @author Nikita Gorodilov
 */
@Component
public class ValidateTokenGatewayFilter implements GatewayFilter {
    @Autowired
    private final ValidateTokenRestTemplate restTemplate;

    public ValidateTokenGatewayFilter(ValidateTokenRestTemplate restTemplate, Environment env) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.defer(() -> {
            String token = getToken(exchange);
            ResponseEntity<String> response = restTemplate.validateToken(token);

            if (response.getStatusCode() == HttpStatus.OK) {
                return chain.filter(exchange);
            } else {
                return Mono.error(new ValidateTokenException(response.getStatusCode(), response.getBody()));
            }
        });
    }

    private String getToken(ServerWebExchange exchange) {
        List<String> tokens = exchange.getRequest().getHeaders().get("X-Access-Token");
        return Optional.ofNullable(tokens).flatMap(t -> t.stream().findFirst()).orElse(null);
    }
}
