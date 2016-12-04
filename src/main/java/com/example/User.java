package com.example;

/**
 * Created by mvukosav on 4.12.2016..
 */
public class User {

    private final String username;
    private final long id;

    public User(String username, long id) {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public long getId() {
        return id;
    }
}
