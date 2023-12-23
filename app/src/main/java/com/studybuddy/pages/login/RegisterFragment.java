package com.studybuddy.pages.login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.studybuddy.R;
import com.studybuddy.pages.HomePage;
import com.studybuddy.storage.ServerData;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class RegisterFragment extends DialogFragment {
    View view;

    public RegisterFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.DialogTheme));
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.fragment_register, null);
        builder.setTitle("Register User")
                .setView(view)
                .setPositiveButton("Register", (dialog, which) -> {
                    HashMap<String, String> values = verifyFields();

                    if (values != null) {
                        registerUser(values);
                    }
                    dismiss();
                })
                .setNegativeButton("Close", (dialog, which) -> {
                    Toast.makeText(view.getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                    dismiss();
                });

        return builder.create();
    }

    private HashMap<String, String> verifyFields() {
        HashMap<String, String> values = new HashMap<>();

        String emailInput = ((EditText) view.findViewById(R.id.email_field)).getText().toString();
        String usernameInput = ((EditText) view.findViewById(R.id.username_field)).getText().toString();
        String firstNameInput = ((EditText) view.findViewById(R.id.first_name_field)).getText().toString();
        String lastNameInput = ((EditText) view.findViewById(R.id.last_name_field)).getText().toString();
        String passwordInput = ((EditText) view.findViewById(R.id.password_field)).getText().toString();
        String passwordVerifyInput = ((EditText) view.findViewById(R.id.password_verify_field)).getText().toString();

        Log.d("exception_caught", "email: " + emailInput);

        if (emailInput.equals("") || usernameInput.equals("") || firstNameInput.equals("") ||
                lastNameInput.equals("") || passwordInput.equals("") || passwordVerifyInput.equals("")) {
            return null;
        }

        if (!passwordInput.equals(passwordVerifyInput)) {
            return null;
        }

        values.put("email", emailInput);
        values.put("username", usernameInput);
        values.put("firstName", firstNameInput);
        values.put("lastName", lastNameInput);
        values.put("password", passwordInput);

        return values;
    }

    private void registerUser(HashMap<String, String> bundle) {
        HashMap<String, String> body = new HashMap<>();
        body.put("email", bundle.get("email"));
        body.put("password", bundle.get("password"));
        body.put("username", bundle.get("username"));
        body.put("first_name", bundle.get("firstName"));
        body.put("last_name", bundle.get("lastName"));

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonObjectRequest request =
                new JsonObjectRequest
                        (
                            Request.Method.POST,
                            ServerData.serverURI + "/auth/register/",
                            new JSONObject(body),
                            response -> {
                                Intent intent = new Intent(view.getContext(), HomePage.class);

                                Toast.makeText(view.getContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                                view.getContext().startActivity(intent);
                            },
                            error -> Toast.makeText(view.getContext(), "Something went wrong.", Toast.LENGTH_LONG).show()
                        );
        requestQueue.add(request);
    }
}
