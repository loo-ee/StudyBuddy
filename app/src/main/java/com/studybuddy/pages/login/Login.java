package com.studybuddy.pages.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.studybuddy.R;
import com.studybuddy.adapters.AuthAdapter;
import com.studybuddy.pages.HomePage;

public class Login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
    }

    public void loginOrRegister(View v) {
        final ProgressBar progressBar = findViewById(R.id.loading);
        @SuppressLint("ShowToast") final Toast waitingToast = Toast.makeText(this, "Redirecting...", Toast.LENGTH_SHORT);

        TextView email = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);
        AuthAdapter.login(this, email.getText().toString(), password.getText().toString());

        waitingToast.show();
        progressBar.setVisibility(View.VISIBLE);
    }
}