package com.studybuddy.pages.my_tasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
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

    @SuppressLint("SetTextI18n")
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
        TextView buddyName = findViewById(R.id.buddy_name);
        TextView status = findViewById(R.id.status);

        taskDescription.setText(this.task.getDescription());
        buddyName.setText(this.buddy.getFirst_name() + " " + this.buddy.getLast_name());

        String statusText = this.userTask.isIs_done() ? "Completed" : "Pending";
        String parsedDate = DateParser.parseDate(this.task.getDate_created().toString());

        date.setText(parsedDate);
        status.setText(statusText);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }
}