package com.studybuddy.models.tasks;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Task {
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("date_created")
    private LocalDate date_created;

    public Task() {}

    public Task(String title, String description, LocalDate date_created) {
        this.title = title;
        this.description = description;
        this.date_created = date_created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate_created() {
        return date_created;
    }

    public void setDate_created(LocalDate date_created) {
        this.date_created = date_created;
    }

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }
}
