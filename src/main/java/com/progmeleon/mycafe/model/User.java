package com.progmeleon.mycafe.model;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String username;
    private String password;
    private UserRole role;

    public User(String name, String username, String password, UserRole role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    // Check if the provided credentials match the user's credentials
    public boolean authenticate(String enteredUsername, String enteredPassword, UserRole enteredRole) {
        return username.equals(enteredUsername) && password.equals(enteredPassword) && role.equals(enteredRole);
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
