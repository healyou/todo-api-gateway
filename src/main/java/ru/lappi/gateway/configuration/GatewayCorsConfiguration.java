package ru.lappi.gateway.configuration;


import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import java.util.HashMap;

/**
 * @author Nikita Gorodilov
 */
@Configuration
public class GatewayCorsConfiguration {
    @Bean
    public CorsConfiguration corsConfiguration(RoutePredicateHandlerMapping routePredicateHandlerMapping) {
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        routePredicateHandlerMapping.setCorsConfigurations(
                new HashMap<>() {{
                    put("/**", corsConfiguration);
                }});
        return corsConfiguration;
    }
}
