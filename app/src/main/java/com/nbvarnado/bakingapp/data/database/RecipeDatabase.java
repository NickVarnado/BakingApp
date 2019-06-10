package com.nbvarnado.bakingapp.data.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.nbvarnado.bakingapp.data.database.recipe.Recipe;
import com.nbvarnado.bakingapp.data.database.recipe.RecipeDao;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    private static final String TAG = RecipeDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DB_NAME = "recipes";
    private static RecipeDatabase sInstance;

    public static RecipeDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance.");
                sInstance = Room.databaseBuilder(context, RecipeDatabase.class, RecipeDatabase.DB_NAME).build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract RecipeDao recipeDao();
}
