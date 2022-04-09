package ru.lappi.gateway.configuration.properties;

import javax.validation.constraints.NotNull;

/**
 * @author Nikita Gorodilov
 */
public class NotesApi {
    @NotNull
    private String baseUrl;
    @NotNull
    private NotesPath path;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public NotesPath getPath() {
        return path;
    }

    public void setPath(NotesPath path) {
        this.path = path;
    }

    public static class NotesPath {
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
