package ru.lappi.gateway.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.lappi.gateway.configuration.properties.ApiProperties;

/**
 * @author Nikita Gorodilov
 */
@Component
public class TestUrlUtils {
    @Autowired
    private final ApiProperties apiProperties;

    public TestUrlUtils(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    /* Всегда доступные urls */
    public String[] getAlwaysAvailableRequestPaths() {
        return new String[] {
                apiProperties.getExternal().getAuth().getPath().getLogin(),
                apiProperties.getExternal().getUsers().getPath().getRegister()
        };
    }

    /* С токеном пускам в /auth-api/** и /notes-api/** */
    public String[] getHasTokenHeaderRequestPaths() {
        return new String[] {
                /* Все url из auth-api c токеном */
                apiProperties.getExternal().getAuth().getPath().getBase() + "/testAllAuthApiUrl",
                /* Все url из notes-api c токеном */
                apiProperties.getExternal().getNotes().getPath().getBase() + "/testAllNotesApiUrl"
        };
    }

    /* url, которые недоступны */
    public String[] getNotFoundRequestPath() {
        return new String[] {
                /* Все url users-api недоступны */
                apiProperties.getExternal().getUsers().getPath().getBase() + "/testAllUsersApiUrl",
                /* Недоступные url */
                "/unknown-api/testAllNotFoundApiUrl"
        };
    }
}
