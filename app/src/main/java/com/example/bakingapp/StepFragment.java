package com.example.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.data.Recipe;

/**
 * Created by Taha on 6/17/2018.
 */

public class StepFragment extends Fragment {

    Recipe.Step mStep;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        if(mStep != null){
            TextView textView = view.findViewById(R.id.test);
            textView.setText(mStep.getDescription());
        }
        return view;
    }

    public void setStep(Recipe.Step mStep) {
        this.mStep = mStep;
    }
}
