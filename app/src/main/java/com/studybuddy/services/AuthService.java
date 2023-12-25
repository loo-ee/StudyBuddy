package com.studybuddy.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.studybuddy.models.auth.Token;
import com.studybuddy.storage.ServerData;
import com.studybuddy.storage.StorageHandler;
import com.studybuddy.storage.TokenHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class AuthService extends Worker {

    public AuthService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            refreshTokens(this.getApplicationContext());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success();
    }

    private void refreshTokens(Context context) throws IOException {
        Token token = TokenHandler.getToken(context);
        HashMap<String, String> body = new HashMap<>();
        body.put("refresh", token.getRefresh());

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest
                (
                        Request.Method.POST,
                        ServerData.serverURI + "/auth/refresh/",
                        new JSONObject(body),
                        response -> {
                            try {
                                StorageHandler.writeToFile(context, response.toString(), "auth");
                            } catch (IOException e) {
                                Log.d("refresh-token", e.toString());
                            }
                        },
                        error -> {
                            Toast.makeText(context, "Session expired", Toast.LENGTH_LONG).show();
                            WorkManager.getInstance(context).cancelAllWorkByTag("refresh-token");
                        }
                );

        requestQueue.add(request);
    }
}
