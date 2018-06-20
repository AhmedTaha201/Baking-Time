package com.example.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.example.bakingapp.data.Recipe;

public class StepActivity extends AppCompatActivity {

    public static final String LOG_TAG = StepActivity.class.getSimpleName();
    public static final String STEP_EXTRA = "step_extra";
    public static final String FRAGMENT = "fragment";
    boolean mPortrait;
    StepFragment mStepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if the phone is in landscape and show full screen
        mPortrait = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        if (!mPortrait) {
            if (getSupportActionBar() != null) getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_step);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(STEP_EXTRA)) {
            Recipe.Step mStep = intent.getParcelableExtra(STEP_EXTRA);

            FragmentManager manager = getSupportFragmentManager();
            if (savedInstanceState != null && savedInstanceState.containsKey(FRAGMENT)) {
                mStepFragment = (StepFragment) manager.getFragment(savedInstanceState, FRAGMENT);
                mStepFragment.setStep(mStep);
                manager.beginTransaction()
                        .replace(R.id.step_container, mStepFragment)
                        .commit();
                return;
            }
            mStepFragment = new StepFragment();
            mStepFragment.setStep(mStep);

            manager.beginTransaction()
                    .add(R.id.step_container, mStepFragment)
                    .commit();
        } else {
            Log.e(LOG_TAG, "The step intent has no extra step");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT, mStepFragment);
    }
}
