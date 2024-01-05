package com.studybuddy.global_fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studybuddy.R;
import com.studybuddy.models.auth.Token;
import com.studybuddy.models.auth.User;
import com.studybuddy.models.tasks.Task;
import com.studybuddy.storage.ServerData;
import com.studybuddy.storage.TokenHandler;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TaskFragment extends Fragment {
    private final ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private View view;
    private Bundle paramsBundle;
    private Task task;
    private User buddy;
    private Token token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_task, container, false);
        paramsBundle = getArguments();

        view.setOnClickListener(clicked -> {
            assert task != null;
            assert buddy != null;
            Bundle taskBundle = new Bundle();
            taskBundle.putSerializable("task", task);
            taskBundle.putSerializable("buddy", buddy);
            Toast.makeText(requireContext(), "Still in progress :P", Toast.LENGTH_LONG).show();
        });

        try {
            this.token = TokenHandler.getToken(requireContext());
            getTaskInfo(requireContext());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this.view;
    }

    private void addTaskToPage(Context context) {
        try {
            assert paramsBundle != null;
            assert task != null;
            assert buddy != null;
            AppCompatActivity activity = (AppCompatActivity) context;
            TextView nameTextView = new TextView(activity);
            TextView ageTextView = new TextView(activity);

            nameTextView.setText(task.getTitle());
            nameTextView.setTextSize(30.f);
            nameTextView.setTextColor(ContextCompat.getColor(context, R.color.cambridge_dark));
            nameTextView.setLayoutParams(layout);

            ageTextView.setText(buddy.getFirst_name());
            ageTextView.setTextSize(20.f);
            ageTextView.setTextColor(ContextCompat.getColor(context, R.color.cambridge_light));
            ageTextView.setLayoutParams(layout);

            ((ViewGroup) view).addView(nameTextView);
            ((ViewGroup) view).addView(ageTextView);
        } catch (Exception e) {
            Log.d("fragment-error", e.toString());
        }
    }

    private void getTaskInfo(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest
                (
                        Request.Method.GET,
                        ServerData.serverURI + "/tasks/get-task/?task_id=" + paramsBundle.getString("task"),
                        null,
                        response -> {
                            try {
                                String responseBody = response.toString();
                                task = mapper.readValue(responseBody, Task.class);
                            } catch (JsonProcessingException e) {
                                Log.d("fragment-error", e.toString());
                            }
                            getUserInfo(context);
                        },
                        error -> {
                            Toast.makeText(context, "Could not load tasks", Toast.LENGTH_SHORT).show();
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

    private void getUserInfo(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest
                (
                        Request.Method.GET,
                        ServerData.serverURI + "/auth/get-user/?user_email=" + paramsBundle.getString("buddy"),
                        null,
                        response -> {
                            try {
                                String responseBody = response.toString();
                                buddy = mapper.readValue(responseBody, User.class);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                            addTaskToPage(context);
                        },
                        error -> {
                            Toast.makeText(context, "Could not load tasks", Toast.LENGTH_SHORT).show();
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
