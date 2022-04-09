package ru.lappi.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.lappi.gateway.configuration.properties.ApiProperties;
import ru.lappi.gateway.utils.StubRequestUtils;
import ru.lappi.gateway.utils.TestUrlUtils;
import ru.lappi.gateway.utils.WebClientUtils;

public class GatewayRoutesTest extends BaseUnitTest {
	@Autowired
	private WebTestClient webClient;
	@Autowired
	private ApiProperties apiProperties;
	@Autowired
	private TestUrlUtils testUrlUtils;
	@Autowired
	private StubRequestUtils stubRequestUtils;
	@Autowired
	private WebClientUtils webClientUtils;

	@Test
	void testAlwaysAvailableUrlRequests() {
		String requestResponseBody = "response_body";
		for (String requestPath: testUrlUtils.getAlwaysAvailableRequestPaths()) {
			stubRequestUtils.stubForJsonRequest(requestPath, requestResponseBody);
		}

		for (String requestPath: testUrlUtils.getAlwaysAvailableRequestPaths()) {
			/* without token header */
			String routeRequestPath = apiProperties.getGatewayBasePath() + requestPath;
			webClientUtils.postForExpectResult(routeRequestPath, HttpStatus.OK, requestResponseBody);

			/* with token header */
			webClientUtils.postWithTokenForExpectResult(routeRequestPath, HttpStatus.OK, requestResponseBody);
		}
	}

	@Test
	void testHasTokenUrlRequestsWithToken() {
		String requestResponseBody = "response_body";
		for (String requestPath: testUrlUtils.getHasTokenHeaderRequestPaths()) {
			stubRequestUtils.stubForJsonRequest(requestPath, requestResponseBody);
		}
		stubRequestUtils.stubForValidateTokenRequest();

		for (String requestPath: testUrlUtils.getHasTokenHeaderRequestPaths()) {
			String routeRequestPath = apiProperties.getGatewayBasePath() + requestPath;
			webClientUtils.postWithTokenForExpectResult(routeRequestPath, HttpStatus.OK, requestResponseBody);
		}
	}

	@Test
	void testHasTokenUrlRequestsWithoutToken() {
		for (String requestPath: testUrlUtils.getHasTokenHeaderRequestPaths()) {
			String routeRequestPath = apiProperties.getGatewayBasePath() + requestPath;
			webClientUtils.postExpectNotFound(routeRequestPath);
		}
	}

	@Test
	void testNotFoundUrlRequests() {
		for (String requestPath: testUrlUtils.getNotFoundRequestPath()) {
			String routeRequestPath = apiProperties.getGatewayBasePath() + requestPath;
			webClientUtils.postExpectNotFound(routeRequestPath);
		}
	}
}
