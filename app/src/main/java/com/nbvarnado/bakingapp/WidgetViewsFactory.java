package com.nbvarnado.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.nbvarnado.bakingapp.data.database.recipe.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int mAppWidgetId;
    private List<Ingredient> mIngredients;

    public WidgetViewsFactory(Context context, Intent intent) {
        this.mContext = context;
        this.mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        this.mIngredients = intent.getParcelableArrayListExtra(RecipeWidgetProvider.EXTRA_INGREDIENTS);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredients == null) return 0;
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || mIngredients == null || mIngredients.get(position) == null) {
            return null;
        }
        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);
        Ingredient ingredient = mIngredients.get(position);
        String ingredientText = ingredient.getQuantity() + " " + ingredient.getMeasure() + " " + ingredient.getIngredient();
        view.setTextViewText(R.id.tv_ingredient, ingredientText);

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
