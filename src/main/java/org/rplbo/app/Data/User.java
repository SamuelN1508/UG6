package org.rplbo.app.Data;

public class User {
    private int id;
    private String username;
    private String email;
    private String role;
    private String password;

    // Constructor
    public User(int id, String username, String role, String password, String email) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.password = password;
        this.email = email;
    }
}
