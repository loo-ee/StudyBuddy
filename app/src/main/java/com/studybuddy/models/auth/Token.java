package com.studybuddy.models.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
    @JsonProperty("access")
    private String access;

    @JsonProperty("refresh")
    private String refresh;

    public Token() {}

    public Token(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
