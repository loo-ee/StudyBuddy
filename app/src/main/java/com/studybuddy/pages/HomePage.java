package com.studybuddy.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.studybuddy.R;
import com.studybuddy.pages.my_tasks.MyTasksPage;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void redirectMyTasks(View view) {
        Intent intent = new Intent(view.getContext(), MyTasksPage.class);
        view.getContext().startActivity(intent);
    }
}