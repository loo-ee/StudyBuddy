package com.studybuddy.pages.my_tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studybuddy.R;
import com.studybuddy.global_fragments.TaskFragment;
import com.studybuddy.models.auth.Token;
import com.studybuddy.models.tasks.UserTask;
import com.studybuddy.storage.LoggedInUser;
import com.studybuddy.storage.MyTasksStorage;
import com.studybuddy.storage.ServerData;
import com.studybuddy.storage.StorageHandler;
import com.studybuddy.storage.TokenHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.jvm.internal.Lambda;

public class MyTasksPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks_page);

        try {
            getUserTasks(this);
        } catch (Exception e) {
            Log.d("exception_caught", e.toString());
        }

    }

    public void getUserTasks(Context context) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Token token = TokenHandler.getToken(context);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request =
                new JsonArrayRequest
                        (
                                Request.Method.GET,
                                ServerData.serverURI + "/tasks/get-user-tasks/?user_email=" + LoggedInUser.getEmail(),
                                null,
                                response -> {
                                    try {
                                        String responseBody = response.toString();
                                        TypeReference<List<UserTask>> typeReference = new TypeReference<List<UserTask>>() {};
                                        List<UserTask> userTasks = mapper.readValue(responseBody, typeReference);
                                        MyTasksStorage.setMyTasks(userTasks);
                                        LinearLayout linearLayout = findViewById(R.id.tasks_container);

                                        FragmentManager fm = getSupportFragmentManager();
                                        FragmentTransaction ft = fm.beginTransaction();

                                        userTasks.forEach(userTask -> {
                                            TaskFragment taskFragment = new TaskFragment();
                                            Bundle bundle = new Bundle();

                                            bundle.putString("task", String.valueOf(userTask.getTask()));
                                            bundle.putString("buddy", String.valueOf(userTask.getBuddy()));

                                            taskFragment.setArguments(bundle);
                                            ft.add(linearLayout.getId(), taskFragment, String.valueOf(userTask.getId()));

                                        });

                                        ft.commit();
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException(e);
                                    }
                                },
                                error -> {
                                    Log.d("exception_caught", String.valueOf(error.networkResponse.statusCode));
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