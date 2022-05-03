package ru.lappi.gateway.configuration.properties;

import javax.validation.constraints.NotNull;

/**
 * @author Nikita Gorodilov
 */
public class AuthApi {
    @NotNull
    private String baseUrl;
    @NotNull
    private AuthPath path;

    public String getLoginUrl() {
        return baseUrl + path.getLogin();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public AuthPath getPath() {
        return path;
    }

    public void setPath(AuthPath path) {
        this.path = path;
    }

    public static class AuthPath {
        @NotNull
        private String base;
        @NotNull
        private String login;
        @NotNull
        private String validateToken;
        @NotNull
        private String refreshToken;

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getValidateToken() {
            return validateToken;
        }

        public void setValidateToken(String validateToken) {
            this.validateToken = validateToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}
