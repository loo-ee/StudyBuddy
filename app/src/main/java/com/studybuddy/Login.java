package com.studybuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = (Button) findViewById(R.id.login);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading);

        loginButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
        });
    }
}