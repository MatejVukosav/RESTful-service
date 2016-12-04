package com.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mvukosav on 4.12.2016..
 */
@Entity
public class Account {

    @OneToMany(mappedBy = "account")
    private Set<Image> images = new HashSet<>();

    @Id
    @GeneratedValue
    private Long id;

    public String username;

    @JsonIgnore
    public String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    Account() {
    }

    public Set<Image> getImages() {
        return images;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }
}
