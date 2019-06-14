package com.nbvarnado.bakingapp;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.nbvarnado.bakingapp.data.database.recipe.Ingredient;
import com.nbvarnado.bakingapp.data.database.recipe.Recipe;
import com.nbvarnado.bakingapp.ui.main.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_INGREDIENTS = "ingredients";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        // Create an Intent to launch MainActivity when clicked
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_recipe, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);

        // Update the recipe name.
        String recipeName = (recipe != null) ? recipe.getName() : "No Recipe Selected";
        views.setTextViewText(R.id.widget_recipe_name, recipeName);

        // Update the ingredients list.
        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        ArrayList<Ingredient> ingredients = (recipe != null) ? (ArrayList) recipe.getIngredients() : new ArrayList<>();
        serviceIntent.putExtra(EXTRA_INGREDIENTS, (Serializable) ingredients);
        serviceIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.widget_ingredients, serviceIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // We're manually updating. Intentionally Blank
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

