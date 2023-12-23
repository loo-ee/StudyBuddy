package com.studybuddy.pages.my_tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.studybuddy.R;
import com.studybuddy.global_fragments.TaskFragment;

public class MyTasksPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks_page);

        LinearLayout linearLayout = findViewById(R.id.tasks_container);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        for (int i = 0; i < 5; i++) {
            TaskFragment taskFragment = new TaskFragment();
            Bundle bundle = new Bundle();

            bundle.putString("name", "Jann");
            bundle.putString("age", "21");

            taskFragment.setArguments(bundle);
            ft.add(linearLayout.getId(), taskFragment, String.valueOf(i));
        }

        ft.commit();
    }
}