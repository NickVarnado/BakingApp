package com.nbvarnado.bakingapp.ui.ingredients;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nbvarnado.bakingapp.R;
import com.nbvarnado.bakingapp.data.database.recipe.Ingredient;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    private List<Ingredient> mIngredients;

    IngredientsAdapter(List<Ingredient> ingredients) {
        this.mIngredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ingredientView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientsViewHolder(ingredientView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);
        String quantity = String.valueOf(ingredient.getQuantity());
        String measure = String.valueOf(ingredient.getMeasure());
        String ingredientText = ingredient.getIngredient();
        String ingredientString = quantity + " " + measure + " " + ingredientText;

        holder.mIngredientTextView.setText(ingredientString);
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) {
            return 0;
        }
        return mIngredients.size();
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {

        final TextView mIngredientTextView;

        public IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            mIngredientTextView = itemView.findViewById(R.id.tv_ingredient);
        }
    }
}
