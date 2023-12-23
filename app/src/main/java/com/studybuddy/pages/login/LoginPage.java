package com.studybuddy.pages.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.studybuddy.R;
import com.studybuddy.pages.my_tasks.MyTasksPage;
import com.studybuddy.storage.ServerData;
import com.studybuddy.pages.HomePage;
import com.studybuddy.storage.StorageHandler;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class LoginPage extends AppCompatActivity {
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        setTitle("Login");

//        Intent intent = new Intent(this, HomePage.class);
//        startActivity(intent);
    }

    public void loginOrRegister(View v) {
        final ProgressBar progressBar = findViewById(R.id.loading);
        @SuppressLint("ShowToast") final Toast waitingToast = Toast.makeText(this, "Redirecting...", Toast.LENGTH_SHORT);
        EditText emailField = findViewById(R.id.email);
        EditText passwordField = findViewById(R.id.password);

        this.email = emailField.getText().toString();
        this.password = passwordField.getText().toString();

        waitingToast.show();
        progressBar.setVisibility(View.VISIBLE);
        login(this);
    }

    public void login(Context context) {
        HashMap<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest
                        (
                            Request.Method.POST,
                            ServerData.serverURI + "/auth/token/",
                            new JSONObject(body),
                            response -> {
                                Intent homePageIntent = new Intent(context, HomePage.class);

                                try {
                                    StorageHandler.writeToFile(context, response.toString(), "auth");
                                    context.startActivity(homePageIntent);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            },
                            error -> {
                                ProgressBar progressBar = ((Activity) context).findViewById(R.id.loading);
                                progressBar.setVisibility(View.GONE);
                                validateEmail(context);
                                Log.d("validate-email", String.valueOf(error.networkResponse.statusCode));
                            }
                        );

        requestQueue.add(request);
    }

    public void validateEmail(Context context) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonObjectRequest request =
                    new JsonObjectRequest
                            (
                                    Request.Method.GET,
                                    ServerData.serverURI + "/auth/validate-email/?email=" + email,
                                    null,
                                    response -> {
                                        Toast.makeText(context, "Password incorrect", Toast.LENGTH_LONG).show();
                                    },
                                    error -> {
                                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);

                                        try {
                                            JSONObject data = new JSONObject(responseBody);
                                            String message = data.optString("message");

                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                            Log.d("validate-email", "from validate error" + error.networkResponse.statusCode);

                                            NewAccountPromptFragment n = new NewAccountPromptFragment();
                                            Bundle bundle = new Bundle();

                                            bundle.putString("email", email);
                                            bundle.putString("password", password);

                                            n.setArguments(bundle);
                                            n.show(getSupportFragmentManager(), "Show Dialog");
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                            );
            requestQueue.add(request);
    }
}