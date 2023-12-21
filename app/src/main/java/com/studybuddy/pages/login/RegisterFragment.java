package com.studybuddy.pages.login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.studybuddy.R;

public class RegisterFragment extends DialogFragment {
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

        builder.setTitle("Register User")
                .setView(inflater.inflate(R.layout.fragment_register, null))
                .setPositiveButton("OK", (dialog, which) -> {
                    Toast.makeText(getContext(), "Confirmed", Toast.LENGTH_LONG).show();
                    dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                    dismiss();
                });

        return builder.create();
    }
}
