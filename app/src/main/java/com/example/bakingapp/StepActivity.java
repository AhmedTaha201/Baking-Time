package com.example.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.bakingapp.data.Recipe;

public class StepActivity extends AppCompatActivity {

    public static final String LOG_TAG = StepActivity.class.getSimpleName();
    public static final String STEP_EXTRA = "step_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(STEP_EXTRA)){
            Recipe.Step mStep = intent.getParcelableExtra(STEP_EXTRA);
            StepFragment stepFragment = new StepFragment();
            stepFragment.setStep(mStep);

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .add(R.id.step_container, stepFragment)
                    .commit();
        }else {
            Log.e(LOG_TAG, "The step intent has no extra step");
        }
    }
}
