package com.example.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.bakingapp.adapters.RecipesAdapter;
import com.example.bakingapp.data.DataService;
import com.example.bakingapp.data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipeListClickListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recipes_recycler_view)
    RecyclerView mRecyclerView;

    RecipesAdapter mRecipesAdapter;

    List<Recipe> mRecipeList;

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

        //Fetching data with the Retrofit Library
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DataService service = retrofit.create(DataService.class);
        Call<List<Recipe>> recipeCall = service.getRecipes();

        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    mRecipeList = response.body();
                    mRecipesAdapter.swapList(mRecipeList);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                t.printStackTrace();
                Log.e(LOG_TAG, "Failed to get Recipe Data");
            }
        });

    }

    @Override
    public void onRecipeCLicked(int position) {
        Intent recipeIntent = new Intent(this, RecipeActivity.class);
        recipeIntent.putExtra(RecipeActivity.RECIPE_EXTRA, mRecipeList.get(position));
        startActivity(recipeIntent);
    }

    ;
}
