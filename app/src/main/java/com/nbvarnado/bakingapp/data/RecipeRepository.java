package com.nbvarnado.bakingapp.data;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nbvarnado.bakingapp.AppExecutors;
import com.nbvarnado.bakingapp.data.database.RecipeDatabase;
import com.nbvarnado.bakingapp.data.database.recipe.Recipe;
import com.nbvarnado.bakingapp.data.database.recipe.RecipeDao;
import com.nbvarnado.bakingapp.data.network.RecipeDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RecipeRepository {

    private static final String TAG = RecipeRepository.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static RecipeRepository sInstance;
    private final RecipeDao mRecipeDao;
    private final RecipeDataSource mRecipeDataSource;
    private final RecipeDatabase mRecipeDatabase;
    private final Context mContext;
    private final AppExecutors mAppExecutors;
    private MutableLiveData<List<Recipe>> recipes;

    private RecipeRepository(Context context,
                             RecipeDatabase database,
                             RecipeDataSource dataSource,
                             AppExecutors appExecutors) {
        mContext = context;
        mRecipeDatabase = database;
        mRecipeDataSource = dataSource;
        mRecipeDao = mRecipeDatabase.recipeDao();
        mAppExecutors = appExecutors;
        recipes = new MutableLiveData<>();
    }

    public synchronized static RecipeRepository getInstance(Context context,
                                                            RecipeDatabase database,
                                                            RecipeDataSource dataSource,
                                                            AppExecutors executors) {
        Log.d(TAG, "Getting repositiory instance");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipeRepository(context, database, dataSource, executors);
                Log.d(TAG, "Repository created.");
            }
        }
        return sInstance;
    }

    /**
     * Uses Retrofit to load the recipe data from the network.
     * @return A list of {@link Recipe}
     */
    private List<Recipe> loadRecipes() {
        Call<List<Recipe>> call = mRecipeDataSource.getService(mContext, mAppExecutors).getRecipes();
        List<Recipe> recipes = new ArrayList<>();
        Response<List<Recipe>> recipeResponse = null;
        try {
            recipeResponse = call.execute();
            if (recipeResponse.body() != null) {
                recipes = recipeResponse.body();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return recipes;
    }

    /**
     * Get's the LiveData object for the recipe with the given id.
     * @param id of the Recipe to retrieve.
     * @return LiveData object for the recipe with the given id.
     */
    public LiveData<Recipe> getRecipeById(int id) {
        LiveData<Recipe> recipe = mRecipeDao.getRecipeById(id);
        return recipe;
    }

    /**
     * Get's the LiveDate object for the recipes. The recipes are queried from the database first.
     * If nothing is returned from the database a network call is executed.
     * @return List of recipes.
     */
    public MutableLiveData<List<Recipe>> getRecipes() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Recipe> recipesFromDb = mRecipeDao.getRecipes();
            // Database is empty. Make Network call.
            if (recipesFromDb == null || recipesFromDb.size() == 0) {
                AppExecutors.getInstance().networkIO().execute(() -> {
                    List<Recipe> recipesFromNetwork = loadRecipes();
                    recipes.postValue(recipesFromNetwork);
                });
            } else {
                recipes.postValue(recipesFromDb);
            }
        });
        return recipes;
    }
}
