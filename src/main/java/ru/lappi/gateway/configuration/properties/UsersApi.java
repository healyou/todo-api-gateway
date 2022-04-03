package ru.lappi.gateway.configuration.properties;

/**
 * @author Nikita Gorodilov
 */
public class UsersApi {
    private String baseUrl;
    private UsersPath path;

    public String getRegisterUrl() {
        return baseUrl + path.getRegister();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public UsersPath getPath() {
        return path;
    }

    public void setPath(UsersPath path) {
        this.path = path;
    }

    public static class UsersPath {
        private String base;
        private String register;

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getRegister() {
            return register;
        }

        public void setRegister(String register) {
            this.register = register;
        }
    }
}
