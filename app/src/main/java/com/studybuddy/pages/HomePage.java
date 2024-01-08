package com.studybuddy.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studybuddy.R;
import com.studybuddy.models.auth.Token;
import com.studybuddy.models.auth.User;
import com.studybuddy.pages.login.LoginPage;
import com.studybuddy.pages.my_tasks.MyTasksPage;
import com.studybuddy.storage.LoggedInUser;
import com.studybuddy.storage.ServerData;
import com.studybuddy.storage.StorageHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        try {
            getUser(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void redirectMyTasks(View view) {
        Intent intent = new Intent(view.getContext(), MyTasksPage.class);
        view.getContext().startActivity(intent);
    }

    public void getUser(Context context) throws IOException {
        if (LoggedInUser.getLoggedInUser() != null)
            return;

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String content = StorageHandler.readFromFile(context, "auth");
        Token token = mapper.readValue(content, Token.class);
        DecodedJWT decodedJWT = JWT.decode(token.getAccess());
        String userEmail = decodedJWT.getClaim("email").asString();

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
                                    } catch (JsonProcessingException e) {
                                        Log.d("exception_caught", e.toString());
                                    }
                                },
                                error -> {
                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context, LoginPage.class);
                                    context.startActivity(intent);
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

    public void logout(View view) throws IOException {
        String nullToken = "{\"refresh\":\"\",\"access\":\"\"}";
        StorageHandler.writeToFile(view.getContext(), nullToken, "auth");
        LoggedInUser.setLoggedInUser(null);
        Toast.makeText(view.getContext(), "Logged out", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(view.getContext(), LoginPage.class);
        view.getContext().startActivity(intent);
    }
}