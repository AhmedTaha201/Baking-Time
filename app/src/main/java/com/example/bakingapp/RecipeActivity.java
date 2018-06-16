package com.example.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.bakingapp.data.Recipe;

/**
 * Created by Taha on 6/14/2018.
 */

public class RecipeActivity extends AppCompatActivity {

    public static final String LOG_TAG = RecipeActivity.class.getSimpleName();

    public static final String RECIPE_EXTRA = "recipe";

    Recipe mRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent recipeIntent = getIntent();
        if (recipeIntent != null && recipeIntent.hasExtra(RECIPE_EXTRA)) {
            mRecipe = recipeIntent.getParcelableExtra(RECIPE_EXTRA);
            Log.e(LOG_TAG, "Got the recipe extra");
        } else {
            Log.e(LOG_TAG, "No extra recipe");
        }
    }
}
