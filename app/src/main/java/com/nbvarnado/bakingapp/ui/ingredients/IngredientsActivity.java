package com.nbvarnado.bakingapp.ui.ingredients;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import com.nbvarnado.bakingapp.R;
import com.nbvarnado.bakingapp.data.database.recipe.Recipe;
import com.nbvarnado.bakingapp.ui.main.MainActivity;

public class IngredientsActivity extends AppCompatActivity {

    private static final String TAG = IngredientsActivity.class.getSimpleName();

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        Toolbar toolbar = findViewById(R.id.toolbar_ingredients);
        setContentView(R.layout.activity_ingredients);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(MainActivity.EXTRA_RECIPE);
        } else {
            Intent intent = getIntent();
            mRecipe = intent.getParcelableExtra(MainActivity.EXTRA_RECIPE);
        }

        TextView toolbarTextView = findViewById(R.id.tv_ingredients_toolbar);
        if (toolbarTextView != null && mRecipe != null) {
            toolbarTextView.setText(mRecipe.getName());
        }
        if (mRecipe != null) {
            RecyclerView recyclerView = findViewById(R.id.rv_ingredients);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(mRecipe.getIngredients());
            recyclerView.setAdapter(ingredientsAdapter);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.EXTRA_RECIPE, mRecipe);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipe = savedInstanceState.getParcelable(MainActivity.EXTRA_RECIPE);
    }

}
