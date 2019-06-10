package com.nbvarnado.bakingapp.data.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nbvarnado.bakingapp.data.database.recipe.Ingredient;
import com.nbvarnado.bakingapp.data.database.recipe.Step;

import java.lang.reflect.Type;
import java.util.List;

public class RecipeTypeConverters {

    @TypeConverter
    public static List<Ingredient> stringToIngredients(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        List<Ingredient> ingredients = gson.fromJson(json, type);
        return ingredients;
    }

    @TypeConverter
    public static String ingredientsToString(List<Ingredient> ingredients) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        String json = gson.toJson(ingredients, type);
        return json;
    }

    @TypeConverter
    public static List<Step> stringToSteps(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        List<Step> steps = gson.fromJson(json, type);
        return steps;
    }

    @TypeConverter
    public static String stepsToString(List<Step> steps) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        String json = gson.toJson(steps, type);
        return json;
    }

}
