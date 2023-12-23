package com.studybuddy.storage;

import com.studybuddy.models.auth.User;

public class LoggedInUser {
    private static User loggedInUser = null;

    public static void setLoggedInUser(User user) {
        LoggedInUser.loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return LoggedInUser.loggedInUser;
    }
}
