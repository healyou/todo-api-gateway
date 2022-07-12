package ru.lappi.gateway.configuration.properties;

import javax.validation.constraints.NotNull;

public class GraphqlApi {
    @NotNull
    private String baseUrl;
    @NotNull
    private GraphqlPath path;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public GraphqlPath getPath() {
        return path;
    }

    public void setPath(GraphqlPath path) {
        this.path = path;
    }

    public static class GraphqlPath {
        @NotNull
        private String base;

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }
    }
}
