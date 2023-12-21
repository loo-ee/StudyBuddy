package com.studybuddy.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.studybuddy.R;
import com.studybuddy.pages.login.RegisterFragment;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        RegisterFragment r = new RegisterFragment();
        r.show(getSupportFragmentManager(), "Show Dialog");
    }
}