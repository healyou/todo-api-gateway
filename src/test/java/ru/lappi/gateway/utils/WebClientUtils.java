package ru.lappi.gateway.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.lappi.gateway.configuration.properties.ApiProperties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Nikita Gorodilov
 */
@Component
public class WebClientUtils {
    @Autowired
    private WebTestClient webClient;
    @Autowired
    private ApiProperties apiProperties;

    public void postForExpectStatus(String path, int expectResponseStatus) {
        webClient
                .post().uri(path)
                .exchange()
                .expectStatus()
                .isEqualTo(expectResponseStatus);
    }

    public void postWithTokenForExpectStatus(String path, int expectResponseStatus) {
        webClient
                .post().uri(path)
                .header(apiProperties.getAccessTokenHeaderCode(), "testAccessToken")
                .exchange()
                .expectStatus()
                .isEqualTo(expectResponseStatus);
    }

    public void postExpectNotFound(String requestPath) {
        webClient
                .post().uri(requestPath)
                .exchange()
                .expectStatus().isNotFound();
    }

    public void postForExpectResult(String requestPath, HttpStatus expectedStatus, String expectedBody) {
        webClient
                .post().uri(requestPath)
                .exchange()
                .expectStatus()
                .isEqualTo(expectedStatus)
                .expectBody()
                .consumeWith(response ->
                        assertThat(response.getResponseBody()).isEqualTo(expectedBody.getBytes())
                );
    }

    public void postWithTokenForExpectResult(String requestPath, HttpStatus expectedStatus, String expectedBody) {
        webClient
                .post().uri(requestPath)
                .header(apiProperties.getAccessTokenHeaderCode(), "testAccessToken")
                .exchange()
                .expectStatus()
                .isEqualTo(expectedStatus)
                .expectBody()
                .consumeWith(response ->
                        assertThat(response.getResponseBody()).isEqualTo(expectedBody.getBytes())
                );
    }
}
