package ru.lappi.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.lappi.gateway.configuration.properties.ApiProperties;
import ru.lappi.gateway.utils.TestUrlUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GatewayRoutesTest extends BaseUnitTest {
	@Autowired
	private WebTestClient webClient;
	@Autowired
	private ApiProperties apiProperties;
	@Autowired
	private TestUrlUtils testUrlUtils;

	@Test
	void testAlwaysAvailableUrlRequests() {
		String requestResponseBody = "response_body";
		for (String requestPath: testUrlUtils.getAlwaysAvailableRequestPaths()) {
			stubForJsonRequest(requestPath, requestResponseBody);
		}

		for (String requestPath: testUrlUtils.getAlwaysAvailableRequestPaths()) {
			/* without token header */
			webClient
					.post().uri(apiProperties.getRoutes().getGatewayBasePath() + requestPath)
					.exchange()
					.expectStatus().isOk()
					.expectBody()
					.consumeWith(response ->
							assertThat(response.getResponseBody()).isEqualTo(requestResponseBody.getBytes())
					);

			/* with token header */
			webClient
					.post().uri(apiProperties.getRoutes().getGatewayBasePath() + requestPath)
					.header(apiProperties.getAccessTokenHeaderCode(), "testAccessToken")
					.exchange()
					.expectStatus().isOk()
					.expectBody()
					.consumeWith(response ->
							assertThat(response.getResponseBody()).isEqualTo(requestResponseBody.getBytes())
					);
		}
	}

	@Test
	void testHasTokenUrlRequestsWithToken() {
		String requestResponseBody = "response_body";
		for (String requestPath: testUrlUtils.getHasTokenHeaderRequestPaths()) {
			stubForJsonRequest(requestPath, requestResponseBody);
		}
		stubForValidateTokenRequest();

		for (String requestPath: testUrlUtils.getHasTokenHeaderRequestPaths()) {
			webClient
					.post().uri(apiProperties.getRoutes().getGatewayBasePath() + requestPath)
					.header(apiProperties.getAccessTokenHeaderCode(), "testAccessToken")
					.exchange()
					.expectStatus().isOk()
					.expectBody()
					.consumeWith(response ->
							assertThat(response.getResponseBody()).isEqualTo(requestResponseBody.getBytes())
					);
		}
	}

	@Test
	void testHasTokenUrlRequestsWithoutToken() {
		for (String requestPath: testUrlUtils.getHasTokenHeaderRequestPaths()) {
			webClient
					.post().uri(apiProperties.getRoutes().getGatewayBasePath() + requestPath)
					.exchange()
					.expectStatus().isNotFound();
		}
	}

	@Test
	void testNotFoundUrlRequests() {
		for (String requestUrl: testUrlUtils.getNotFoundRequestPath()) {
			webClient
					.post().uri(apiProperties.getRoutes().getGatewayBasePath() + requestUrl)
					.exchange()
					.expectStatus().isNotFound();
		}
	}

	private void stubForValidateTokenRequest() {
		String validateTokenPath = apiProperties.getExternal().getAuth().getPath().getValidateToken();
		stubFor(post(urlEqualTo(validateTokenPath))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withStatus(HttpStatus.OK.value())
						.withBody("success")
				)
		);
	}

	private void stubForJsonRequest(String requestPath, String bodyResult) {
		stubFor(post(urlEqualTo(requestPath))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withStatus(HttpStatus.OK.value())
						.withBody(bodyResult)
				)
		);
	}
}
