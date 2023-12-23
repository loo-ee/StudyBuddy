package com.studybuddy.pages.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.studybuddy.R;

public class NewAccountPromptFragment extends DialogFragment {
    public NewAccountPromptFragment() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.DialogTheme));

        assert this.getArguments() != null;
        String emailAddress = this.getArguments().getString("email");

        builder.setTitle("Create new account?")
                .setMessage(emailAddress + " is not registered. Create account instead?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    RegisterFragment registerFragment = new RegisterFragment();
                    Bundle bundle = this.getArguments();

                    registerFragment.setArguments(bundle);
                    registerFragment.show(requireActivity().getSupportFragmentManager(), "Show Dialog");
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dismiss();
                });

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_login_page, container, false);
    }
}
