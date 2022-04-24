package ru.lappi.gateway.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;

/**
 * @author Nikita Gorodilov
 */
@Validated
@Configuration
@ConfigurationProperties("cors")
public class GatewayCorsConfiguration {
    @Size(min=1)
    private List<String> allowedOrigins;

    @Bean
    public CorsConfiguration corsConfiguration(RoutePredicateHandlerMapping routePredicateHandlerMapping) {
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        allowedOrigins.forEach(corsConfiguration::addAllowedOrigin);
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        routePredicateHandlerMapping.setCorsConfigurations(
                new HashMap<>() {{
                    put("/**", corsConfiguration);
                }});
        return corsConfiguration;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}
