package com.studybuddy.pages.my_tasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.studybuddy.R;
import com.studybuddy.models.auth.User;
import com.studybuddy.models.tasks.Task;
import com.studybuddy.models.tasks.UserTask;
import com.studybuddy.util.DateParser;

public class TaskPage extends AppCompatActivity {
    private UserTask userTask;
    private Task task;
    private User buddy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_page);

        this.userTask = (UserTask) getIntent().getSerializableExtra("userTask");
        this.task = (Task) getIntent().getSerializableExtra("task");
        this.buddy = (User) getIntent().getSerializableExtra("buddy");

        setTitle(this.task.getTitle());

        TextView date = findViewById(R.id.date);
        TextView taskDescription = findViewById(R.id.taskDescription);

        taskDescription.setText(this.task.getDescription());

        String parsedDate = DateParser.parseDate(this.task.getDate_created().toString());
        date.setText(parsedDate);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }
}