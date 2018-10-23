package com.parents2parents.kindergarten;

public class Kindergarten {
    private final String id;
    private final String url;
    private final String description;
    private final String userId;

    public Kindergarten(String url, String description, String userId) {

        this(null, url, description, userId);
    }

    public Kindergarten(String id, String url, String description, String userId) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }
}