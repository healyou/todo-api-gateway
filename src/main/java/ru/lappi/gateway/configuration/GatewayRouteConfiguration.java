package ru.lappi.gateway.configuration;

import io.netty.handler.logging.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;
import ru.lappi.gateway.configuration.properties.*;
import ru.lappi.gateway.token.ValidateTokenGatewayFilter;

/**
 * @author Nikita Gorodilov
 */
@Configuration
public class GatewayRouteConfiguration {
    @Autowired
    private final ApiProperties apiProperties;
    @Autowired
    private final ValidateTokenGatewayFilter validateTokenGatewayFilter;

    public GatewayRouteConfiguration(ApiProperties apiProperties, ValidateTokenGatewayFilter validateTokenGatewayFilter) {
        this.apiProperties = apiProperties;
        this.validateTokenGatewayFilter = validateTokenGatewayFilter;
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        routes = configureAlwaysAvailableRoutes(routes);
        routes = configureHasTokenRoutes(routes);
        return routes.build();
    }

    private RouteLocatorBuilder.Builder configureAlwaysAvailableRoutes(RouteLocatorBuilder.Builder builder) {
        UsersApi usersApi = apiProperties.getExternal().getUsers();
        AuthApi authApi = apiProperties.getExternal().getAuth();

        String authApiUrl = authApi.getBaseUrl();
        String usersApiUrl =usersApi.getBaseUrl();

        String gatewayBasePath = apiProperties.getRoutes().getGatewayBasePath();
        /* /tod_o-web-api/auth-api/login */
        String loginRoutePattern = gatewayBasePath + authApi.getPath().getLogin();
        /* /tod_o-web-api/auth-api/refreshToken */
        String refreshTokenRoutePattern = gatewayBasePath + authApi.getPath().getRefreshToken();
        /* /tod_o-web-api/users-api/users/register */
        String registerRoutePattern = gatewayBasePath + usersApi.getPath().getRegister();

        return builder
                .route("ALWAYS_AVAILABLE_LOGIN_ROUTE", p -> p
                        .path(loginRoutePattern)
                        .filters(f -> f
                                .circuitBreaker(config ->
                                        config.setName(CircuitBreakerConfiguration.AUTH_API_CIRCUIT_BREAKER_NAME)
                                )
                                .rewritePath(gatewayBasePath + "/(?<segment>.*)", "/$\\{segment}")
                        )
                        .uri(authApiUrl)
                )
                .route("ALWAYS_AVAILABLE_REFRESH_TOKEN_ROUTE", p -> p
                        .path(refreshTokenRoutePattern)
                        .filters(f -> f
                                .circuitBreaker(config ->
                                        config.setName(CircuitBreakerConfiguration.AUTH_API_CIRCUIT_BREAKER_NAME)
                                )
                                .rewritePath(gatewayBasePath + "/(?<segment>.*)", "/$\\{segment}")
                        )
                        .uri(authApiUrl)
                )
                .route("ALWAYS_AVAILABLE_REGISTER_ROUTE", p -> p
                        .path(registerRoutePattern)
                        .filters(f -> f
                                .circuitBreaker(config ->
                                        config.setName(CircuitBreakerConfiguration.USERS_API_CIRCUIT_BREAKER_NAME)
                                )
                                .rewritePath(gatewayBasePath + "/(?<segment>.*)", "/$\\{segment}")
                        )
                        .uri(usersApiUrl)
                );
    }

    private RouteLocatorBuilder.Builder configureHasTokenRoutes(RouteLocatorBuilder.Builder builder) {
        NotesApi notesApi = apiProperties.getExternal().getNotes();
        AuthApi authApi = apiProperties.getExternal().getAuth();
        GraphqlApi graphqlApi = apiProperties.getExternal().getGraphql();

        String authApiUrl = authApi.getBaseUrl();
        String notesApiUrl = notesApi.getBaseUrl();
        String graphqlApiUrl = graphqlApi.getBaseUrl();
        String accessTokenHeaderCode = apiProperties.getAccessTokenHeaderCode();

        String gatewayBasePath = apiProperties.getRoutes().getGatewayBasePath();
        /* /tod_o-web-api/notes-api/** */
        String notesApiPattern = gatewayBasePath + notesApi .getPath().getBase() + "/**";
        /* /tod_o-web-api/auth-api/** */
        String authApiPattern = gatewayBasePath + authApi.getPath().getBase() + "/**";
        /* /tod_o-web-api/graphql/** */
        String graphqlApiPattern = gatewayBasePath + graphqlApi.getPath().getBase() + "/**";
        return builder
                .route("HAS_TOKEN_NOTES_API_ROUTE", p -> p
                        .path(notesApiPattern)
                        .and()
                        .header(accessTokenHeaderCode)
                        .filters(f -> f
                                .circuitBreaker(config ->
                                        config.setName(CircuitBreakerConfiguration.NOTES_API_CIRCUIT_BREAKER_NAME)
                                )
                                .filter(validateTokenGatewayFilter)
                                .rewritePath(gatewayBasePath + "/(?<segment>.*)", "/$\\{segment}")
                        )
                        .uri(notesApiUrl)
                )
                .route("HAS_TOKEN_AUTH_API_ROUTE", p -> p
                        .path(authApiPattern)
                        .and()
                        .header(accessTokenHeaderCode)
                        .filters(f -> f
                                .circuitBreaker(config ->
                                        config.setName(CircuitBreakerConfiguration.AUTH_API_CIRCUIT_BREAKER_NAME)
                                )
                                .filter(validateTokenGatewayFilter)
                                .rewritePath(gatewayBasePath + "/(?<segment>.*)", "/$\\{segment}")
                        )
                        .uri(authApiUrl)
                )
                .route("HAS_TOKEN_GRAPHQL_API_ROUTE", p -> p
                        .path(graphqlApiPattern)
                        .and()
                        .header(accessTokenHeaderCode)
                        .filters(f -> f
                                .circuitBreaker(config ->
                                        config.setName(CircuitBreakerConfiguration.GRAPHQL_API_CIRCUIT_BREAKER_NAME)
                                )
                                .filter(validateTokenGatewayFilter)
                                .rewritePath(gatewayBasePath + "/(?<segment>.*)", "/$\\{segment}")
                        )
                        .uri(graphqlApiUrl)
                );
    }

    @Bean
    HttpClient httpClient() {
        return HttpClient.create().wiretap("LoggingFilter", LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);
    }
}
