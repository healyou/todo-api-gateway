package ru.lappi.gateway.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.lappi.gateway.configuration.properties.CircuitBreakerProperties;
import ru.lappi.gateway.token.ValidateTokenException;

import java.time.Duration;

/**
 * @author Nikita Gorodilov
 */
@Configuration
public class CircuitBreakerConfiguration {
    public static final String AUTH_API_CIRCUIT_BREAKER_NAME = "authApiCircuitBreaker";
    public static final String USERS_API_CIRCUIT_BREAKER_NAME = "usersApiCircuitBreaker";
    public static final String NOTES_API_CIRCUIT_BREAKER_NAME = "usersApiCircuitBreaker";

    public static final String CIRCUIT_BREAKER_SLIDING_WINDOW_SIZE_CODE = "circuitbreaker.slidingWindowSize";
    public static final String CIRCUIT_BREAKER_MINIMUM_NUMBERS_OF_CALLS_CODE = "circuitbreaker.minimumNumberOfCalls";

    @Autowired
    private final CircuitBreakerProperties circuitBreakerProperties;

    public CircuitBreakerConfiguration(CircuitBreakerProperties circuitBreakerProperties) {
        this.circuitBreakerProperties = circuitBreakerProperties;
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                /* Переход по состояниям по кол-ву запросов */
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                /* Минимальное кол-во вызовов для перехода в состояние open */
                .slidingWindowSize(circuitBreakerProperties.getSlidingWindowSize())
                /* Для вычисления рейтинга ошибок */
                .minimumNumberOfCalls(circuitBreakerProperties.getMinimumNumberOfCalls())
                /* Процент ошибочных запросов, чтобы перейти в открытое состояние */
                .failureRateThreshold(CircuitBreakerConfig.DEFAULT_FAILURE_RATE_THRESHOLD)
                /* Длительность медленного запроса - будет ошибкой */
                .slowCallDurationThreshold(
                        Duration.ofMillis(circuitBreakerProperties.getSlowCallDurationThresholdMillis())
                )
                /* Количество медленных запросов для перехода в открытое состоение */
                .slowCallRateThreshold(CircuitBreakerConfig.DEFAULT_SLOW_CALL_RATE_THRESHOLD)
                /* Время, которое находимся в состоянии open */
                .waitDurationInOpenState(
                        Duration.ofSeconds(CircuitBreakerConfig.DEFAULT_WAIT_DURATION_IN_OPEN_STATE)
                )
                /* По истечению времени автоматом переходить в состояние half open */
                .enableAutomaticTransitionFromOpenToHalfOpen()
                /* Кол-во вызовов в half open состоянии - для перехода в другое состояние */
                .permittedNumberOfCallsInHalfOpenState(CircuitBreakerConfig.DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN_STATE)
                /* Валидация токена не должна создавать ошибку для circuit breaker */
                .ignoreException(throwable -> throwable instanceof ValidateTokenException)
                .build();

        /* Максимальное время операции, по истечению которого будет ошибка - Timeout */
        int maxOperationTimeSeconds = 30;
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(circuitBreakerConfig)
                .timeLimiterConfig(
                        TimeLimiterConfig.custom().timeoutDuration(
                                Duration.ofSeconds(maxOperationTimeSeconds)
                        )
                        .build()
                )
                .build());
    }
}
