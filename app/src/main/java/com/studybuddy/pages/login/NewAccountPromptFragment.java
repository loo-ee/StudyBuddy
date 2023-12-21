package com.studybuddy.pages.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.studybuddy.R;

public class NewAccountPromptFragment extends DialogFragment {
    private String emailAddress = "";
    private AppCompatActivity appCompatActivity = null;

    public NewAccountPromptFragment() {
        super();
    }

    public NewAccountPromptFragment(String emailAddress, AppCompatActivity appCompatActivity) {
        super();
        this.emailAddress = emailAddress;
        this.appCompatActivity = appCompatActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.DialogTheme));
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setTitle("Create new account?")
                .setMessage(this.emailAddress + " is not registered. Create account instead?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    RegisterFragment registerFragment = new RegisterFragment();
                    registerFragment.show(appCompatActivity.getSupportFragmentManager(), "Show Dialog");
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dismiss();
                });

        return builder.create();
    }
}
