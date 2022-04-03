package ru.lappi.gateway.configuration.properties;

/**
 * @author Nikita Gorodilov
 */
public class External {
    private AuthApi auth;
    private UsersApi users;
    private NotesApi notes;

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
}
