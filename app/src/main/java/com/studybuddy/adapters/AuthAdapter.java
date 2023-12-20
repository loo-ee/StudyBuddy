package com.studybuddy.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studybuddy.R;
import com.studybuddy.models.auth.Token;
import com.studybuddy.pages.HomePage;
import com.studybuddy.storage.StorageHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class AuthAdapter {

    public static void login(Context context, String email, String password) {
        ObjectMapper mapper = new ObjectMapper();

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> body = new HashMap<>();
                body.put("email", email);
                body.put("password", password);

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                JsonObjectRequest request =
                        new JsonObjectRequest(Request.Method.POST, ServerData.serverURI + "/auth/token/", new JSONObject(body), future, error -> {
                            @SuppressLint("ShowToast") Toast failedToast = Toast.makeText(context, "Account not found", Toast.LENGTH_LONG);
                            ProgressBar progressBar = ((Activity) context).findViewById(R.id.loading);

                            failedToast.show();
                            progressBar.setVisibility(View.GONE);
                        } ) {
                            @Override
                            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                                if (response.statusCode == 200) {
                                    Intent homePageIntent = new Intent(context, HomePage.class);
                                    context.startActivity(homePageIntent);
                                }

                                return super.parseNetworkResponse(response);
                            }
                        };

                requestQueue.add(request);

                try {
                    JSONObject response = future.get();
                    Token token = mapper.readValue(response.toString(), Token.class);
                    String[] data = {token.getAccess(), token.getRefresh()};
                    StorageHandler.writeToFile(context, data, "auth");
                } catch (InterruptedException | ExecutionException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
