package com.nbvarnado.bakingapp.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nbvarnado.bakingapp.data.RecipeRepository;
import com.nbvarnado.bakingapp.data.database.recipe.Recipe;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private final RecipeRepository mRepository;
    private MutableLiveData<List<Recipe>> mRecipes;

    MainActivityViewModel(RecipeRepository repository) {
        mRepository = repository;
    }

    public LiveData<List<Recipe>> getRecipes() {
        if (mRecipes == null) {
            mRecipes = mRepository.getRecipes();
        }
        return mRecipes;
    }
}
