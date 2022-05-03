package ru.lappi.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.contract.spec.internal.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import ru.lappi.gateway.configuration.properties.ApiProperties;
import ru.lappi.gateway.configuration.properties.CircuitBreakerProperties;
import ru.lappi.gateway.utils.StubRequestUtils;
import ru.lappi.gateway.utils.TestUrlUtils;
import ru.lappi.gateway.utils.WebClientUtils;

/**
 * @author Nikita Gorodilov
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CircuitBreakerTest extends BaseUnitTest {
    @Autowired
    private WebClientUtils webClientUtils;
    @Autowired
    private ApiProperties apiProperties;
    @Autowired
    private TestUrlUtils testUrlUtils;
    @Autowired
    private CircuitBreakerProperties circuitBreakerProperties;
    @Autowired
    private StubRequestUtils stubRequestUtils;
    @Autowired
    private ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory;

    @Test
    void testCircuitBreakerSetOpenStateAfterErrorCalls() {
        /* Медленный для circuit breaker ответ */
        stubRequestUtils.stubAllForCircuitBreakerSlowCall();

        /* n медленных запросов */
        String loginRequestPath = apiProperties.getGatewayAuthLoginPath();
        for (int i = 0; i < circuitBreakerProperties.getMinimumNumberOfCalls(); i++) {
            webClientUtils.postForExpectStatus(loginRequestPath, HttpStatus.OK);
        }

        /* После перехода в open state - 503 ошибка */
        webClientUtils.postForExpectStatus(loginRequestPath, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    void testLoginAndValidateTokenUseSameCircuitBreaker() {
        stubRequestUtils.stubAllForCircuitBreakerSlowCall();

        /* auth api call */
        String loginRequestPath = apiProperties.getGatewayAuthLoginPath();
        webClientUtils.postForExpectStatus(loginRequestPath, HttpStatus.OK);

        /* auth api validate token before get user notes */
        String notesApiWithValidateTokenPath = apiProperties.getGatewayBasePath() +
                testUrlUtils.getTestNotesApiPath();
        webClientUtils.postWithTokenForExpectStatus(notesApiWithValidateTokenPath, HttpStatus.OK);

        /* После перехода в open state - 503 ошибка */
        webClientUtils.postWithTokenForExpectStatus(loginRequestPath, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    void testLoginAndRefreshTokenUseSameCircuitBreaker() {
        stubRequestUtils.stubAllForCircuitBreakerSlowCall();

        /* auth api call */
        String loginRequestPath = apiProperties.getGatewayAuthLoginPath();
        webClientUtils.postForExpectStatus(loginRequestPath, HttpStatus.OK);
        /* auth api call */
        String refreshTokenRequestPath = apiProperties.getGatewayAuthRefreshTokenPath();
        webClientUtils.postForExpectStatus(refreshTokenRequestPath, HttpStatus.OK);

        /* 3 запрос -> После перехода в open state - 503 ошибка */
        webClientUtils.postWithTokenForExpectStatus(loginRequestPath, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    void testAuthTokenAndNotesTokenUseNotSameCircuitBreaker() {
        stubRequestUtils.stubAllForCircuitBreakerSlowCall();

        /* auth cb call -> 1 request */
        String loginRequestPath = apiProperties.getGatewayAuthLoginPath();
        for (int i = 0; i < circuitBreakerProperties.getMinimumNumberOfCalls() / 2; i++) {
            webClientUtils.postForExpectStatus(loginRequestPath, HttpStatus.OK);
        }

        /* auth cb call + notes cb call -> 2 request */
        String notesApiWithValidateTokenPath = apiProperties.getGatewayBasePath() +
                testUrlUtils.getTestNotesApiPath();
        for (int i = 0; i < circuitBreakerProperties.getMinimumNumberOfCalls() / 2; i++) {
            /* Если auth запросы и notes запросы по одному CB - то тут не будет OK */
            webClientUtils.postWithTokenForExpectStatus(notesApiWithValidateTokenPath, HttpStatus.OK);
        }
        /* auth cb превысил лимит ошибок */
        webClientUtils.postWithTokenForExpectStatus(notesApiWithValidateTokenPath, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
