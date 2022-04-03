package ru.lappi.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableConfigurationProperties
@RestController
// TODO логирование
// TODO добавить spring cloud netflix eureka
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just("fallback");
	}

	// TODO добавить circuit
	@RequestMapping("/notFoundServiceFallback")
	public Mono<String> notFoundServiceFallback() {
		return Mono.just("notFoundServiceFallback");
	}
}
