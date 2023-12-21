package com.studybuddy.pages.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RegisterFragment extends DialogFragment {
    public RegisterFragment() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Register User");
        builder.setMessage("Please Register");

        builder.setPositiveButton("OK", (dialog, which) -> {
            Toast.makeText(getContext(), "Confirmed", Toast.LENGTH_LONG).show();
            dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            dismiss();
        });
        return builder.create();
    }
}
