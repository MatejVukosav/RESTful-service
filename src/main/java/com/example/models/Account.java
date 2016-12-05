package com.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvukosav on 4.12.2016..
 */
@Entity
public class Account {

    @OneToMany(mappedBy = "account")
    private List<Image> images = new ArrayList<>();

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

//    public Set<Image> getImages() {
//        return images;
//    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

//    public void setImages(Set<Image> images) {
//        this.images = images;
//    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Image> getImages() {
        return images;
    }
}
