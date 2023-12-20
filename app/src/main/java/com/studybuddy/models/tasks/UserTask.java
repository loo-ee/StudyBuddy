package com.studybuddy.models.tasks;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class UserTask {
    @JsonProperty("task")
    private long task;

    @JsonProperty("owner")
    private long owner;

    @JsonProperty("buddy")
    private long buddy;

    @JsonProperty("is_done")
    private boolean is_done;

    @JsonProperty("date_finished")
    private LocalDate date_finished;

    public UserTask() {}

    public UserTask(long task, long owner, long buddy, boolean is_done, LocalDate date_finished) {
        this.task = task;
        this.owner = owner;
        this.buddy = buddy;
        this.is_done = is_done;
        this.date_finished = date_finished;
    }

    public long getTask() {
        return task;
    }

    public void setTask(long task) {
        this.task = task;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public long getBuddy() {
        return buddy;
    }

    public void setBuddy(long buddy) {
        this.buddy = buddy;
    }

    public boolean isIs_done() {
        return is_done;
    }

    public void setIs_done(boolean is_done) {
        this.is_done = is_done;
    }

    public LocalDate getDate_finished() {
        return date_finished;
    }

    public void setDate_finished(LocalDate date_finished) {
        this.date_finished = date_finished;
    }

    @NonNull
    @Override
    public String toString() {
        return this.task + " of " + this.owner;
    }
}
