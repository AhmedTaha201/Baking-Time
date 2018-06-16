package com.example.bakingapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.data.Recipe;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;

/**
 * Created by Taha on 6/16/2018.
 */

public class RecipeFragment extends Fragment {

    private Recipe mRecipe;

    public RecipeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        final FlexboxLayout layout = rootView.findViewById(R.id.chips_group_layout);
        showIngridientChips(mRecipe.getIngredients(), layout);
        return rootView;
    }

    public void setRecipe(Recipe mRecipe) {
        this.mRecipe = mRecipe;
    }

    public void showIngridientChips(List<Recipe.Ingredient> ingredients, ViewGroup layout) {
        List<String> ingridientsList = new ArrayList<>();
        for (Recipe.Ingredient i : ingredients) {
            String ingridientString = i.getIngredient();//+ " (" + String.valueOf(i.getQuantity() + " " + i.getMeasure() + ")");
            ingridientsList.add(ingridientString);
        }

        ChipCloudConfig config = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .useInsetPadding(true)
                .checkedChipColor(Color.parseColor("#ddaa00"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#efefef"))
                .uncheckedTextColor(Color.parseColor("#666666"));

        ChipCloud chips = new ChipCloud(getActivity(), layout, config);
        chips.addChips(ingridientsList);
    }

}
