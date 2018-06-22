package com.example.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakingapp.data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {

    public static final String LOG_TAG = StepActivity.class.getSimpleName();
    public static final String STEP_LIST_EXTRA = "step_extra";
    public static final String STEP_POSITION_EXTRA = "position_extra";
    public static final String FRAGMENT = "fragment";
    boolean mPortrait;
    StepFragment mStepFragment;

    FragmentManager mFragmentManager;

    List<Recipe.Step> mStepList;
    @Nullable
    @BindView(R.id.back_btn)
    ImageView mBackButton;
    @Nullable
    @BindView(R.id.navigation_text_view)
    TextView mStepName;
    @Nullable
    @BindView(R.id.forward_btn)
    ImageView mForwardButton;
    private int mPosition;
    private int mMaxPosition;

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
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(STEP_LIST_EXTRA)) {
            mStepList = intent.getParcelableArrayListExtra(STEP_LIST_EXTRA);
            mMaxPosition = mStepList.size() - 1;
            mPosition = intent.getIntExtra(STEP_POSITION_EXTRA, 0);

            mFragmentManager = getSupportFragmentManager();
            if (savedInstanceState != null && savedInstanceState.containsKey(FRAGMENT)) {
                if (mPortrait) setupNavigation();
                mStepFragment = (StepFragment) mFragmentManager.getFragment(savedInstanceState, FRAGMENT);
                mStepFragment.setStep(mStepList.get(mPosition));
                mFragmentManager.beginTransaction()
                        .replace(R.id.step_container, mStepFragment)
                        .commit();
                return;
            }
            addFragmentWithPosition(mPosition);
            setupNavigation();
        } else {
            Log.e(LOG_TAG, "The step intent has no extra step");
        }
    }

    private void addFragmentWithPosition(int position) {
        mStepFragment = new StepFragment();
        mStepFragment.setStep(mStepList.get(position));
        mFragmentManager.beginTransaction()
                .replace(R.id.step_container, mStepFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT, mStepFragment);
    }

    private void setupNavigation() {
        if (mStepName != null) mStepName.setText(mStepList.get(mPosition).getShortDescription());

        if (mForwardButton == null || mBackButton == null) return;
        if (mPosition == mMaxPosition) mForwardButton.setImageResource(R.drawable.forward_grey);
        if (mPosition == 0) mBackButton.setImageResource(R.drawable.back_grey);
        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition < mMaxPosition) {
                    mPosition++;
                    if (mStepName != null)
                        mStepName.setText(mStepList.get(mPosition).getShortDescription());
                    if (mPosition == mMaxPosition)
                        mForwardButton.setImageResource(R.drawable.forward_grey);
                    if (mPosition > 0) mBackButton.setImageResource(R.drawable.back);
                    addFragmentWithPosition(mPosition);
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition > 0) {
                    mPosition--;
                    if (mStepName != null)
                        mStepName.setText(mStepList.get(mPosition).getShortDescription());
                    if (mPosition == 0) mBackButton.setImageResource(R.drawable.back_grey);
                    if (mPosition < mMaxPosition)
                        mForwardButton.setImageResource(R.drawable.forward);
                    addFragmentWithPosition(mPosition);
                }
            }
        });
    }
}
