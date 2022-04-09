package ru.lappi.gateway.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.lappi.gateway.configuration.properties.ApiProperties;
import ru.lappi.gateway.configuration.properties.CircuitBreakerProperties;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * @author Nikita Gorodilov
 */
@Component
public class StubRequestUtils {
    @Autowired
    private ApiProperties apiProperties;
    @Autowired
    private CircuitBreakerProperties circuitBreakerProperties;

    public void stubAllForDelayResponse(int delayMillis) {
        stubFor(any(anyUrl())
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withFixedDelay(delayMillis)
                )
        );
    }

    public void stubAllForCircuitBreakerSlowCall() {
        int delayMillisOnSlowCallError = circuitBreakerProperties.getSlowCallDurationThresholdMillis() + 1000;
        stubFor(any(anyUrl())
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withFixedDelay(delayMillisOnSlowCallError)
                )
        );
    }

    public void stubForJsonRequest(String requestPath, String bodyResult) {
        stubFor(post(urlEqualTo(requestPath))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.OK.value())
                        .withBody(bodyResult)
                )
        );
    }

    public void stubForValidateTokenRequest() {
        String validateTokenPath = apiProperties.getExternal().getAuth().getPath().getValidateToken();
        stubFor(post(urlEqualTo(validateTokenPath))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.OK.value())
                        .withBody("success")
                )
        );
    }
}
