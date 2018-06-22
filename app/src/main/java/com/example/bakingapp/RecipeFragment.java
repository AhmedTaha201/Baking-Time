package com.example.bakingapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    public final static String RECIPE_NAME_EXTRA = "extra_name";
    StepAdapter mStepAdapter;
    List<Recipe.Step> mSteps = null;
    @Nullable
    onFragmentStepClicked mCallback;
    private Recipe mRecipe;

    public RecipeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        final FlexboxLayout layout = rootView.findViewById(R.id.chips_group_layout);
        showIngridientChips(mRecipe.getIngredients(), layout);


        if (mRecipe != null) {
            mSteps = mRecipe.getSteps();
        }
        mStepAdapter = new StepAdapter(getActivity(), mSteps, this);

        RecyclerView stepsRecyclerView = rootView.findViewById(R.id.steps_recycler_view);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        stepsRecyclerView.setAdapter(mStepAdapter);
        stepsRecyclerView.setNestedScrollingEnabled(false);
        return rootView;
    }

    public void setRecipe(Recipe mRecipe) {
        this.mRecipe = mRecipe;
    }

    public void setCallback(onFragmentStepClicked callback) {
        this.mCallback = callback;
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

        if (RecipeActivity.mTwoPane && mCallback != null) {
            mCallback.onFragmentStepClicked(position);
        } else {
            Intent stepIntent = new Intent(getActivity(), StepActivity.class);
            //Passing the whole list of steps to support navigation
            stepIntent.putParcelableArrayListExtra(StepActivity.STEP_LIST_EXTRA, (ArrayList<Recipe.Step>) mSteps);
            stepIntent.putExtra(StepActivity.STEP_POSITION_EXTRA, position);
            stepIntent.putExtra(RECIPE_NAME_EXTRA, mRecipe.getName());
            startActivity(stepIntent);
        }
    }

    //In two pane mode
    public interface onFragmentStepClicked {
        void onFragmentStepClicked(int position);
    }
}
