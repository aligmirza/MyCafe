package com.progmeleon.mycafe.model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Check if the provided credentials match the user's credentials
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // Change username
    public void changeUsername(String newUsername) {
        this.username = newUsername;
    }

    // Change password
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // Check if the provided username is the same as the current username
    public boolean hasSameUsername(String usernameToCompare) {
        return this.username.equalsIgnoreCase(usernameToCompare);
    }
}
