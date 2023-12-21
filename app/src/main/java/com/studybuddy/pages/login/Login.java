package com.studybuddy.pages.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studybuddy.R;
import com.studybuddy.storage.ServerData;
import com.studybuddy.models.auth.Token;
import com.studybuddy.pages.HomePage;
import com.studybuddy.storage.StorageHandler;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

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
        login(this, email.getText().toString(), password.getText().toString());

        waitingToast.show();
        progressBar.setVisibility(View.VISIBLE);
    }

    public void login(Context context, String email, String password) {
        ObjectMapper mapper = new ObjectMapper();

        new Thread(() -> {
            HashMap<String, String> body = new HashMap<>();
            body.put("email", email);
            body.put("password", password);

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request =
                    new JsonObjectRequest
                            (
                                    Request.Method.POST,
                                    ServerData.serverURI + "/auth/token/",
                                    new JSONObject(body),
                                    future,
                                    error -> {
                                        ProgressBar progressBar = ((Activity) context).findViewById(R.id.loading);
                                        progressBar.setVisibility(View.GONE);
                                        validateEmail(context, email);
                                        Log.d("validate-email", String.valueOf(error.networkResponse.statusCode));
                                    }
                            ) {
                        @Override
                        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                            if (response.statusCode == 200) {
                                Intent homePageIntent = new Intent(context, HomePage.class);
                                String responseBody = new String(response.data, StandardCharsets.UTF_8);

                                try {
                                    JSONObject responseData = new JSONObject(responseBody);
                                    Token token = mapper.readValue(responseData.toString(), Token.class);
                                    String[] data = {token.getAccess(), token.getRefresh()};
                                    StorageHandler.writeToFile(context, data, "auth");
                                    context.startActivity(homePageIntent);
                                } catch (JSONException | IOException e) {
                                    throw new RuntimeException(e);
                                }

                            }

                            return super.parseNetworkResponse(response);
                        }
                    };

            requestQueue.add(request);
        }).start();
    }

    public void validateEmail(Context context, String email) {
        new Thread(() -> {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request =
                    new JsonObjectRequest
                            (
                                    Request.Method.GET,
                                    ServerData.serverURI + "/auth/validate-email/?email=" + email,
                                    null,
                                    future,
                                    error -> {
                                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);

                                        try {
                                            JSONObject data = new JSONObject(responseBody);
                                            String message = data.optString("message");

                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                            Log.d("validate-email", "from validate error" + error.networkResponse.statusCode);

                                            AppCompatActivity activity = (AppCompatActivity) context;
                                            NewAccountPromptFragment n = new NewAccountPromptFragment(email, activity);

                                            n.show(activity.getSupportFragmentManager(), "Show Dialog");
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                            ) {
                        @Override
                        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                            if (response.statusCode == 200) {
                                Looper.prepare();
                                Toast.makeText(context, "Password incorrect", Toast.LENGTH_LONG).show();
                            }
                            return super.parseNetworkResponse(response);
                        }
                    };

            requestQueue.add(request);
        }).start();
    }
}