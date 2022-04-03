package ru.lappi.gateway.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nikita Gorodilov
 */
@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
    private String accessTokenHeaderCode;
    private External external;
    private Routes routes;

    public String getAccessTokenHeaderCode() {
        return accessTokenHeaderCode;
    }

    public void setAccessTokenHeaderCode(String accessTokenHeaderCode) {
        this.accessTokenHeaderCode = accessTokenHeaderCode;
    }

    public External getExternal() {
        return external;
    }

    public void setExternal(External external) {
        this.external = external;
    }

    public Routes getRoutes() {
        return routes;
    }

    public void setRoutes(Routes routes) {
        this.routes = routes;
    }
}
