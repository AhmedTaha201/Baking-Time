package com.example.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.bakingapp.data.Recipe;

/**
 * Created by Taha on 6/14/2018.
 */

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.onFragmentStepClicked {

    public static final String LOG_TAG = RecipeActivity.class.getSimpleName();
    public static final String RECIPE_EXTRA = "recipe";
    public static final String BUNDLE_KEY_RECIPE_FRAGMENT = "recipe_fragment_key";
    public static final String BUNDLE_KEY_STEP_FRAGMENT = "step_fragment_key";
    public static final String BUNDLE_KEY_STEP_POSITION = "step_position_key";
    public static boolean mTwoPane;
    FragmentManager mFragmentManager;
    Recipe mRecipe;
    RecipeFragment mRecipeFragment;
    StepFragment mStepFragment;
    int mStepPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mTwoPane = getResources().getConfiguration().screenWidthDp >= 600;

        Intent recipeIntent = getIntent();
        if (recipeIntent != null && recipeIntent.hasExtra(RECIPE_EXTRA)) {
            mRecipe = recipeIntent.getParcelableExtra(RECIPE_EXTRA);
            Log.e(LOG_TAG, "Got the recipe extra");
            //Set recipe name
            if (!mTwoPane) setTitle(mRecipe.getName());

            mFragmentManager = getSupportFragmentManager();
            if (savedInstanceState == null) {
                mStepPosition = 0;
                mRecipeFragment = new RecipeFragment();
                mRecipeFragment.setRecipe(mRecipe);
                if (mTwoPane) mRecipeFragment.setCallback(this);
                mFragmentManager.beginTransaction()
                        .add(R.id.recipe_container, mRecipeFragment)
                        .commit();

                if (mTwoPane) {
                    addStepFragmentWithPosition(mStepPosition);
                }
            } else if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY_RECIPE_FRAGMENT)) {
                mStepPosition = savedInstanceState.getInt(BUNDLE_KEY_STEP_POSITION, 0);
                mRecipeFragment = (RecipeFragment) mFragmentManager.getFragment(savedInstanceState, BUNDLE_KEY_RECIPE_FRAGMENT);
                mRecipeFragment.setRecipe(mRecipe);
                if (mTwoPane) mRecipeFragment.setCallback(this);

                mFragmentManager.beginTransaction()
                        .replace(R.id.recipe_container, mRecipeFragment)
                        .commit();

                if (mTwoPane && savedInstanceState.containsKey(BUNDLE_KEY_STEP_FRAGMENT)) {
                    mStepFragment = (StepFragment) mFragmentManager.getFragment(savedInstanceState, BUNDLE_KEY_STEP_FRAGMENT);
                    mStepFragment.setStep(mRecipe.getSteps().get(mStepPosition));
                    mFragmentManager.beginTransaction()
                            .replace(R.id.step_container, mStepFragment)
                            .commit();

                }

            }
        } else {
            Log.e(LOG_TAG, "No extra recipe");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_KEY_STEP_POSITION, mStepPosition);
        getSupportFragmentManager().putFragment(outState, BUNDLE_KEY_RECIPE_FRAGMENT, mRecipeFragment);
        if (mTwoPane)
            getSupportFragmentManager().putFragment(outState, BUNDLE_KEY_STEP_FRAGMENT, mStepFragment);

    }

    private void addStepFragmentWithPosition(int position) {
        mStepPosition = position;
        mStepFragment = new StepFragment();
        mStepFragment.setStep(mRecipe.getSteps().get(mStepPosition));
        mFragmentManager.beginTransaction()
                .replace(R.id.step_container, mStepFragment)
                .commit();

    }

    @Override
    public void onFragmentStepClicked(int position) {
        addStepFragmentWithPosition(position);
    }
}
