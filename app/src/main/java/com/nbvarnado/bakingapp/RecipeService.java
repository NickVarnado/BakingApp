package com.nbvarnado.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.nbvarnado.bakingapp.data.database.recipe.Recipe;

public class RecipeService extends IntentService {

    public static final String ACTION_SET_RECIPE = "com.nbvarnado.bakingapp.action.set_recipe";
    public static final String EXTRA_RECIPE = "com.nbvarnado.bakingapp.extra.recipe";

    public RecipeService() {
        super(RecipeService.class.getSimpleName());
    }

    public static void setActiveRecipe(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeService.class);
        intent.setAction(ACTION_SET_RECIPE);
        intent.putExtra(EXTRA_RECIPE, recipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SET_RECIPE.equals(action)) {
                final Recipe recipe = intent.getParcelableExtra(EXTRA_RECIPE);
                handleSetActiveRecipe(recipe);
            }
        }
    }

    private void handleSetActiveRecipe(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, recipe, appWidgetIds);
    }
}
