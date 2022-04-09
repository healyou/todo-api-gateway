package ru.lappi.gateway.configuration.properties;

import javax.validation.constraints.NotNull;

/**
 * @author Nikita Gorodilov
 */
public class Routes {
    @NotNull
    private String gatewayBasePath;
    @NotNull
    private String validateTokenUrl;

    public String getGatewayBasePath() {
        return gatewayBasePath;
    }

    public void setGatewayBasePath(String gatewayBasePath) {
        this.gatewayBasePath = gatewayBasePath;
    }

    public String getValidateTokenUrl() {
        return validateTokenUrl;
    }

    public void setValidateTokenUrl(String validateTokenUrl) {
        this.validateTokenUrl = validateTokenUrl;
    }
}