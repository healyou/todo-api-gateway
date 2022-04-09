package ru.lappi.gateway.token.resttempate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.lappi.gateway.configuration.properties.ApiProperties;

/**
 * @author Nikita Gorodilov
 */
@Component
public class ValidateTokenRestTemplate extends RestTemplate {
    private final String validateTokenUrl;
    private final String accessTokenHeaderCode;

    public ValidateTokenRestTemplate(ApiProperties apiProperties) {
        super();
        setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        /* Все сообщения от сервиса не будут считаться ошибками response */
        setErrorHandler(new NoErrorResponseHandler());
        this.validateTokenUrl = apiProperties.getRoutes().getValidateTokenUrl();
        this.accessTokenHeaderCode = apiProperties.getAccessTokenHeaderCode();
    }

    public ResponseEntity<String> validateToken(String token) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (token != null) {
            headers.add(accessTokenHeaderCode, token);
        }

        HttpEntity<String> request =
                new HttpEntity<>(null, headers);

        return postForEntity(
                validateTokenUrl,
                request,
                String.class
        );
    }
}
