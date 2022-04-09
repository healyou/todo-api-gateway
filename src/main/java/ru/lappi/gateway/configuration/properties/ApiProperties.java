package ru.lappi.gateway.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author Nikita Gorodilov
 */
@Validated
@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
    @NotNull
    private String accessTokenHeaderCode;
    @NotNull
    private External external;
    @NotNull
    private Routes routes;

    public String getGatewayAuthLoginPath() {
        return getGatewayBasePath() + getExternalLoginPath();
    }

    public String getGatewayBasePath() {
        return getRoutes().getGatewayBasePath();
    }

    public String getExternalLoginPath() {
        return getExternal().getAuth().getPath().getLogin();
    }

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
