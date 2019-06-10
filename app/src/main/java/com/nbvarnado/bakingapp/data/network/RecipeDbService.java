package com.nbvarnado.bakingapp.data.network;

import com.nbvarnado.bakingapp.data.database.recipe.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeDbService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
