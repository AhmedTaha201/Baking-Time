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

public class RecipeActivity extends AppCompatActivity {

    public static final String LOG_TAG = RecipeActivity.class.getSimpleName();

    public static final String RECIPE_EXTRA = "recipe";
    public static final String BUNDLE_KEY_RECIPE_FRAGMENT = "recipe_fragment_key";
    Recipe mRecipe;
    RecipeFragment mRecipeFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent recipeIntent = getIntent();
        if (recipeIntent != null && recipeIntent.hasExtra(RECIPE_EXTRA)) {
            mRecipe = recipeIntent.getParcelableExtra(RECIPE_EXTRA);
            Log.e(LOG_TAG, "Got the recipe extra");

            FragmentManager manager = getSupportFragmentManager();
            if (savedInstanceState == null) {
                mRecipeFragment = new RecipeFragment();
                mRecipeFragment.setRecipe(mRecipe);

                manager.beginTransaction()
                        .add(R.id.recipe_container, mRecipeFragment)
                        .commit();
            } else if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY_RECIPE_FRAGMENT)) {
                mRecipeFragment = (RecipeFragment) manager.getFragment(savedInstanceState, BUNDLE_KEY_RECIPE_FRAGMENT);
                mRecipeFragment.setRecipe(mRecipe);

                manager.beginTransaction()
                        .replace(R.id.recipe_container, mRecipeFragment)
                        .commit();
            }
        } else {
            Log.e(LOG_TAG, "No extra recipe");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, BUNDLE_KEY_RECIPE_FRAGMENT, mRecipeFragment);
    }
}
