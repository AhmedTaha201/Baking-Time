package com.example.bakingapp.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Taha on 6/13/2018.
 */

public interface DataService {

    @GET ("baking.json")
    Call<List<Recipe>> getRecipes();
}
