package com.studybuddy.pages.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.studybuddy.adapters.UnirestConfig;
import com.studybuddy.pages.HomePage;
import com.studybuddy.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        UnirestConfig.init();
    }

    public void loginOrRegister(View v) {
        final ProgressBar progressBar = findViewById(R.id.loading);
        @SuppressLint("ShowToast") final Toast toast = Toast.makeText(this, "Redirecting...", Toast.LENGTH_LONG);

        toast.show();
        progressBar.setVisibility(View.VISIBLE);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Intent homePageIntent = new Intent(this, HomePage.class);
        startActivity(homePageIntent);
    }
}