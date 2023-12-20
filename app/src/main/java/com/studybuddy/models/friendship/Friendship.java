package com.studybuddy.models.friendship;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Friendship {
    @JsonProperty("user")
    private long user;

    @JsonProperty("friend")
    private long friend;

    @JsonProperty("status")
    private String status;

    public Friendship() {}

    public Friendship(long user, long friend, String status) {
        this.user = user;
        this.friend = friend;
        this.status = status;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getFriend() {
        return friend;
    }

    public void setFriend(long friend) {
        this.friend = friend;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return this.user + " is " + this.status + " with " + this.friend;
    }
}
