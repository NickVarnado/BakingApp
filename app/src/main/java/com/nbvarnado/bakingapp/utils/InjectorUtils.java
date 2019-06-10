package com.nbvarnado.bakingapp.utils;

import android.content.Context;

import com.nbvarnado.bakingapp.AppExecutors;
import com.nbvarnado.bakingapp.data.RecipeRepository;
import com.nbvarnado.bakingapp.data.database.RecipeDatabase;
import com.nbvarnado.bakingapp.data.network.RecipeDataSource;
import com.nbvarnado.bakingapp.ui.main.MainViewModelFactory;

public class InjectorUtils {

    public static RecipeRepository provideRepository(Context context) {
        RecipeDatabase database = RecipeDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        RecipeDataSource recipeDataSource = RecipeDataSource.getInstance(context.getApplicationContext(), executors);
        return RecipeRepository.getInstance(context, database, recipeDataSource, executors);
    }

    public static MainViewModelFactory provideMainViewModelFactory(Context context) {
        RecipeRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

}
