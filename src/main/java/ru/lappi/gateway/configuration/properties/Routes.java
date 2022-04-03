package ru.lappi.gateway.configuration.properties;

/**
 * @author Nikita Gorodilov
 */
public class Routes {
    private String gatewayBasePath;
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