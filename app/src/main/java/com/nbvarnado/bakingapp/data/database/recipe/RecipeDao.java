package com.nbvarnado.bakingapp.data.database.recipe;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe_table")
    List<Recipe> getRecipes();

    @Query("SELECT * FROM recipe_table WHERE id = :recipeId")
    LiveData<Recipe> getRecipeById(int recipeId);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(Recipe recipe);
}
