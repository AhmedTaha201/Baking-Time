package com.example.bakingapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.adapters.StepAdapter;
import com.example.bakingapp.data.Recipe;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;

/**
 * Created by Taha on 6/16/2018.
 */

public class RecipeFragment extends Fragment implements StepAdapter.StepItemClickListener {

    private Recipe mRecipe;
    StepAdapter mStepAdapter;

    public RecipeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        final FlexboxLayout layout = rootView.findViewById(R.id.chips_group_layout);
        showIngridientChips(mRecipe.getIngredients(), layout);

        List<Recipe.Step> steps = null;
        if (mRecipe != null) {
            steps = mRecipe.getSteps();
        }
        mStepAdapter = new StepAdapter(getActivity(), steps, this);

        RecyclerView stepsRecyclerView = rootView.findViewById(R.id.steps_recycler_view);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        stepsRecyclerView.setAdapter(mStepAdapter);
        stepsRecyclerView.setNestedScrollingEnabled(false);
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

    @Override
    public void onStepClick(int position) {
        Log.d("RecipeFragment", "Step "  + String.valueOf(position) + " clicked!");
    }
}
