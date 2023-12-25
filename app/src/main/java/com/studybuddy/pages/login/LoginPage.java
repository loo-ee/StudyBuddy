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
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studybuddy.R;
import com.studybuddy.models.auth.Token;
import com.studybuddy.models.auth.User;
import com.studybuddy.pages.my_tasks.MyTasksPage;
import com.studybuddy.services.AuthService;
import com.studybuddy.storage.LoggedInUser;
import com.studybuddy.storage.ServerData;
import com.studybuddy.pages.HomePage;
import com.studybuddy.storage.StorageHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginPage extends AppCompatActivity {
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        setTitle("Login");

        if (LoggedInUser.getLoggedInUser() == null) {
            PeriodicWorkRequest refreshTokenWorkRequest = new PeriodicWorkRequest.Builder
                    (AuthService.class, 15, TimeUnit.MINUTES)
                    .addTag("refresh-token")
                    .build();

            WorkManager.getInstance(this).enqueueUniquePeriodicWork("refreshTokenWork", ExistingPeriodicWorkPolicy.KEEP, refreshTokenWorkRequest);
        }

        try {
            getUser(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private void login(Context context) {
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
                                try {
                                    Intent homePageIntent = new Intent(context, HomePage.class);
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

    private void validateEmail(Context context) {
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
                                        } catch (Exception e) {
                                            Log.d("refresh-token", e.toString());
                                        }
                                    }
                            );
            requestQueue.add(request);
    }

    public void getUser(Context context) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String userEmail = "";
        String content = StorageHandler.readFromFile(context, "auth");
        Token token = mapper.readValue(content, Token.class);

        try {
            DecodedJWT decodedJWT = JWT.decode(token.getAccess());
            userEmail = decodedJWT.getClaim("email").asString();
         } catch (JWTDecodeException e) {
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest
                        (
                                Request.Method.GET,
                                ServerData.serverURI + "/auth/get-user/?user_email=" + userEmail,
                                null,
                                response -> {
                                    try {
                                        String responseBody = response.toString();
                                        User foundUser = mapper.readValue(responseBody, User.class);
                                        LoggedInUser.setLoggedInUser(foundUser);
                                        Intent intent = new Intent(context, HomePage.class);
                                        context.startActivity(intent);
                                    } catch (JsonProcessingException e) {
                                        Log.d("exception_caught", e.toString());
                                    }
                                },
                                error -> {
                                    Toast.makeText(context, "Session expired", Toast.LENGTH_LONG).show();
                                }
                        ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + token.getAccess());
                        return headers;
                    }
                };

        requestQueue.add(request);
    }
}