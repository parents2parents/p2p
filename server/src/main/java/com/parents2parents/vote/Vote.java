package com.parents2parents.vote;

import java.time.ZonedDateTime;

public class Vote {
    private final String id;
    private final ZonedDateTime createdAt;
    private final String userId;
    private final String kindergartenId;

    public Vote(ZonedDateTime createdAt, String userId, String kindergartenId) {
        this(null, createdAt, userId, kindergartenId);
    }

    public Vote(String id, ZonedDateTime createdAt, String userId, String kindergartenId) {
        this.id = id;
        this.createdAt = createdAt;
        this.userId = userId;
        this.kindergartenId = kindergartenId;
    }

    public String getId() {
        return id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getKindergartenId() {
        return kindergartenId;
    }
}