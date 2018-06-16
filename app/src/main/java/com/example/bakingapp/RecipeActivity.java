package com.example.bakingapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.bakingapp.data.Recipe;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipListener;

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
            showIngridientChips(mRecipe.getIngredients());
            Log.e(LOG_TAG, "Got the recipe extra");
        } else {
            Log.e(LOG_TAG, "No extra recipe");
        }
    }

    public void showIngridientChips(List<Recipe.Ingredient> ingredients) {
        List<String> ingridientsList = new ArrayList<>();
        for (Recipe.Ingredient i : ingredients) {
            String ingridientString = i.getIngredient() ;//+ " (" + String.valueOf(i.getQuantity() + " " + i.getMeasure() + ")");
            ingridientsList.add(ingridientString);
        }

        final FlexboxLayout layout = (FlexboxLayout) findViewById(R.id.chips_group_layout);

        ChipCloudConfig config = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .useInsetPadding(true)
                .checkedChipColor(Color.parseColor("#ddaa00"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#efefef"))
                .uncheckedTextColor(Color.parseColor("#666666"));

        final ChipCloud chips = new ChipCloud(this, layout, config);
        chips.addChips(ingridientsList);
    }
}
