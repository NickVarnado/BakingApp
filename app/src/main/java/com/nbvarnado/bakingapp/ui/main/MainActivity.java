package com.nbvarnado.bakingapp.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nbvarnado.bakingapp.IdlingResource.SimpleIdlingResource;
import com.nbvarnado.bakingapp.R;
import com.nbvarnado.bakingapp.RecipeService;
import com.nbvarnado.bakingapp.data.database.recipe.Recipe;
import com.nbvarnado.bakingapp.ui.ingredients.IngredientsActivity;
import com.nbvarnado.bakingapp.ui.recipe.StepListActivity;
import com.nbvarnado.bakingapp.utils.InjectorUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickListener, RecipeAdapter.IngredientsClickListener {

    private final String TAG = MainActivity.class.getSimpleName();

    // Intent Extras
    public static final String EXTRA_RECIPE = "extra_recipe";

    // Views
    @BindView(R.id.rv_recipes) RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading) ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;

    private RecipeAdapter mRecipeAdapter;
    private MainActivityViewModel mMainActivityViewModel;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        MainViewModelFactory factory = InjectorUtils.provideMainViewModelFactory(this);
        mMainActivityViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);
        mMainActivityViewModel.getRecipes().observe(this, recipes -> {
            if (recipes != null) {
                mRecipeAdapter.setRecipeData(recipes);
                mRecipeAdapter.notifyDataSetChanged();
                showRecipes();
            } else {
                showError();
            }
        });

        mRecyclerView.setHasFixedSize(true);

        int spanCount = calculateNumberOfColumns(getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,
                spanCount,
                RecyclerView.VERTICAL,
                false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecipeAdapter = new RecipeAdapter(this, this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        showLoading();
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, StepListActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        RecipeService.setActiveRecipe(this, recipe);
        startActivity(intent);
    }

    @Override
    public void onIngredientsClick(Recipe recipe) {
        Intent intent = new Intent(this, IngredientsActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        startActivity(intent);
    }

    public static int calculateNumberOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 600;
        int spanCount = (int) (dpWidth / scalingFactor);
        if (spanCount < 1) return 1;
        return spanCount;
    }

    /**
     * Show the ProgressBar while loading the data.
     */
    private void showLoading() {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * Show the RecyclerView when the data is loaded.
     */
    private void showRecipes() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    /**
     * Show the error message onFailure.
     */
    private void showError() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

}
