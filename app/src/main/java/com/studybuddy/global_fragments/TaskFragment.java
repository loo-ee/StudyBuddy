package com.studybuddy.global_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.studybuddy.R;

public class TaskFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        TextView nameTextView = new TextView(getActivity());
        TextView ageTextView = new TextView(getActivity());
        Bundle params = getArguments();

        ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        assert params != null;
        nameTextView.setText(params.getString("task"));
        nameTextView.setTextSize(30.f);
        nameTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.cambridge_dark));
        nameTextView.setLayoutParams(layout);

        ageTextView.setText(params.getString("buddy"));
        ageTextView.setTextSize(20.f);
        ageTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.cambridge_light));
        ageTextView.setLayoutParams(layout);

        ((ViewGroup) view).addView(nameTextView);
        ((ViewGroup) view).addView(ageTextView);

        return view;
    }
}
