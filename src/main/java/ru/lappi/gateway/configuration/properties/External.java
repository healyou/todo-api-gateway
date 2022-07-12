package ru.lappi.gateway.configuration.properties;

import javax.validation.constraints.NotNull;

/**
 * @author Nikita Gorodilov
 */
public class External {
    @NotNull
    private AuthApi auth;
    @NotNull
    private UsersApi users;
    @NotNull
    private NotesApi notes;
    @NotNull
    private GraphqlApi graphql;

    public AuthApi getAuth() {
        return auth;
    }

    public void setAuth(AuthApi auth) {
        this.auth = auth;
    }

    public UsersApi getUsers() {
        return users;
    }

    public void setUsers(UsersApi users) {
        this.users = users;
    }

    public NotesApi getNotes() {
        return notes;
    }

    public void setNotes(NotesApi notes) {
        this.notes = notes;
    }

    public GraphqlApi getGraphql() {
        return graphql;
    }

    public void setGraphql(GraphqlApi graphql) {
        this.graphql = graphql;
    }
}
