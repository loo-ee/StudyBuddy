package com.studybuddy.storage;

import com.studybuddy.models.tasks.UserTask;
import java.util.List;

public class MyTasksStorage {
    private static List<UserTask> myTasks = null;

    public static void setMyTasks(List<UserTask> tasks) {
        MyTasksStorage.myTasks = tasks;
    }

    public static List<UserTask> getMyTasks() {
        return MyTasksStorage.myTasks;
    }
}
