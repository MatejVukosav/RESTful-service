package com.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by mvukosav on 4.12.2016..
 */
@Entity
public class Image {

    Image() {
    }

    @JsonIgnore
    @ManyToOne
    private Account account;

    @Id
    @GeneratedValue
    private Long id;

    public String name;

    public Image(Account account, String name) {
        this.account = account;
        this.name = name;
    }

    public Account getAccount() {
        return account;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
