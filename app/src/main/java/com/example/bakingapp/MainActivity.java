package com.example.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.bakingapp.adapters.RecipesAdapter;
import com.example.bakingapp.data.DataService;
import com.example.bakingapp.data.Recipe;
import com.example.bakingapp.data.RecipeWidgetProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipeListClickListener {

    public static final String BUNDLE_KEY_RECIPE_LIST = "recipe_list_key";
    public static String SP_KEY_INGREDIENTS = "ingredients_key";
    public static String SP_KEY_NAME = "name_key";
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recipes_recycler_view)
    RecyclerView mRecyclerView;
    RecipesAdapter mRecipesAdapter;
    List<Recipe> mRecipeList;

    @Nullable
    SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);


        mRecipesAdapter = new RecipesAdapter(this, null, this);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mRecipesAdapter);

        if (savedInstanceState == null) {
            //Fetching data with the Retrofit Library
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DataService service = retrofit.create(DataService.class);
            Call<List<Recipe>> recipeCall = service.getRecipes();

            //Set the IdlingState to false before enqueueing the call
            getIdlingResource().setIdleState(false);
            recipeCall.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    if (response.isSuccessful()) {
                        mRecipeList = response.body();
                        mRecipesAdapter.swapList(mRecipeList);
                        //It`s ok to start the UI test
                        getIdlingResource().setIdleState(true);
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    t.printStackTrace();
                    Log.e(LOG_TAG, "Failed to get Recipe Data");
                }
            });
        } else if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY_RECIPE_LIST)) {
            mRecipeList = savedInstanceState.getParcelableArrayList(BUNDLE_KEY_RECIPE_LIST);
            mRecipesAdapter.swapList(mRecipeList);
        }

    }

    @Override
    public void onRecipeCLicked(int position) {
        saveIngredientsInPreferences(position);

        //Update app widgets
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_ingredient_list);
        RecipeWidgetProvider.updateWidgets(this, manager, appWidgetIds);

        Intent recipeIntent = new Intent(this, RecipeActivity.class);
        recipeIntent.putExtra(RecipeActivity.RECIPE_EXTRA, mRecipeList.get(position));
        startActivity(recipeIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_KEY_RECIPE_LIST, (ArrayList<Recipe>) mRecipeList);
    }

    //A helper method to save the ingredients of the last clicked recipe into the sharedPreferences to show in the app widget
    private void saveIngredientsInPreferences(int position) {
        //Save the last clicked recipe ingredients in shared preferences
        Recipe recipe = mRecipeList.get(position);
        String name = recipe.getName();
        Set<String> ingredientsSet = getIngredientsSet(recipe);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SP_KEY_NAME, name).apply();
        editor.putStringSet(SP_KEY_INGREDIENTS, ingredientsSet).apply();
    }

    //A helper method to get ingredients list in a set of strings to store in shared preferences
    public Set<String> getIngredientsSet(Recipe recipe) {
        //Turn the ingredients list into a set of strings
        List<Recipe.Ingredient> ingredients = recipe.getIngredients();
        Set<String> ingredientsSet = new HashSet<>();
        for (Recipe.Ingredient i : ingredients) {
            String ingredientString = i.getIngredient() + " (" + String.valueOf(i.getQuantity() + " " + i.getMeasure() + ")");
            ingredientsSet.add(ingredientString);
        }
        return ingredientsSet;
    }

    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
