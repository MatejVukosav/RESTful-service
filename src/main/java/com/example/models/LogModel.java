package com.example.models;

/**
 * Created by mvukosav on 5.12.2016..
 */
public class LogModel {

    String userAgent;
    String resourcePath;

    public LogModel(String userAgent, String resourcePath) {
        this.userAgent = userAgent;
        this.resourcePath = resourcePath;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    @Override
    public String toString() {
        return resourcePath + "  " + userAgent;
    }
}
