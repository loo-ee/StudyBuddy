package com.studybuddy.adapters;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studybuddy.models.auth.Token;

import org.json.JSONObject;

import java.util.HashMap;

public class AuthAdapter {

    public static void login(Context context, String email, String password) {
        HashMap<String, String> body = new HashMap<>();

        body.put("email", email);
        body.put("password", password);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.POST, ServerData.serverURI + "/auth/token/", new JSONObject(body),
                        response -> {
                            Log.d("json_response", response.toString());
                            String resBody = response.toString();
                            ObjectMapper objectMapper = new ObjectMapper();
                            try {
                                Token token = objectMapper.readValue(resBody, Token.class);
                                Log.d("json_response", token.getAccess());
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        error -> {

                        });

        requestQueue.add(request);
    }
}
