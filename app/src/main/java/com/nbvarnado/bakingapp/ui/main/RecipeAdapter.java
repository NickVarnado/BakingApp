package com.nbvarnado.bakingapp.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nbvarnado.bakingapp.R;
import com.nbvarnado.bakingapp.data.database.recipe.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;

    private final RecipeClickListener mOnRecipeClickListener;
    private final IngredientsClickListener mOnIngredientsClickListener;

    public interface RecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public interface IngredientsClickListener {
        void onIngredientsClick(Recipe recipe);
    }

    RecipeAdapter(RecipeClickListener recipeListener, IngredientsClickListener ingredientsListener) {
        mOnRecipeClickListener = recipeListener;
        mOnIngredientsClickListener = ingredientsListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recipeCardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new RecipeViewHolder(recipeCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        String imageUrl = recipe.getImage();

        // Picasso will throw an exception if the image url is an empty string.
        if (imageUrl.isEmpty()) {
            holder.mImageView.setImageResource(R.drawable.recipe_error);
        } else {
            Picasso.get()
                .load(imageUrl)
                .error(R.drawable.recipe_error)
                .into(holder.mImageView);
        }

        holder.mNameTextView.setText(recipe.getName());
        holder.mServings.setText(String.valueOf(recipe.getServings()));
        holder.mIngredientsButton.setOnClickListener(view -> mOnIngredientsClickListener.onIngredientsClick(recipe));
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) {
            return 0;
        }
        return mRecipes.size();
    }

    void setRecipeData(List<Recipe> recipeData) {
        mRecipes = recipeData;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mImageView;
        final TextView mNameTextView;
        final TextView mServings;
        final Button mStepsButton;
        final Button mIngredientsButton;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_recipe_card);
            mNameTextView = itemView.findViewById(R.id.tv_name);
            mServings = itemView.findViewById(R.id.tv_servings);
            mStepsButton = itemView.findViewById(R.id.button_steps);
            mIngredientsButton = itemView.findViewById(R.id.button_ingredients);
            itemView.setOnClickListener(this);
            mStepsButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Recipe recipe = mRecipes.get(position);
            mOnRecipeClickListener.onRecipeClick(recipe);
        }
    }

}
