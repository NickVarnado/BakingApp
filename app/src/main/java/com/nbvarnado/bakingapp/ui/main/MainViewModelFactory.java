package com.nbvarnado.bakingapp.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.nbvarnado.bakingapp.data.RecipeRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final RecipeRepository mRecipeRepository;

    public MainViewModelFactory(RecipeRepository repository) {
        this.mRecipeRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(mRecipeRepository);
    }
}
