package com.progmeleon.mycafe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    public static int lastId = 0;

    private int id;
    private String name;
    private String username;
    private String password;
    private UserRole role;

    public User(@JsonProperty("name") String name,@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("role") UserRole role) {
        this.id = ++lastId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @JsonIgnore
    public User(@JsonProperty("name") String name,@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public User(String customerName) {
        this.name = customerName;
    }

    public User(int userId, String name, String username, String password, UserRole userRole) {
        this.id = userId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = userRole;
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

    // Check if the provided username is the same as the current username
    public boolean hasSameUsername(String usernameToCompare) {
        return this.username.equalsIgnoreCase(usernameToCompare);
    }

    public static void updateLastId(int id) {
        if (id > lastId) {
            lastId = id;
        }
    }

    public static void setLastId(List<User> users) {
        for (User user : users) {
            updateLastId(user.getId());
        }
    }
}
