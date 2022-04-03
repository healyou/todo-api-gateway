package ru.lappi.gateway.configuration.properties;

/**
 * @author Nikita Gorodilov
 */
public class AuthApi {
    private String baseUrl;
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
        private String base;
        private String login;
        private String validateToken;

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
    }
}
