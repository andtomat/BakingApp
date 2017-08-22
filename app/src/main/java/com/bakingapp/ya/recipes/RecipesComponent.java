package com.bakingapp.ya.recipes;

import com.bakingapp.ya.data.RecipesRepositoryComponent;
import com.bakingapp.ya.util.FragmentScoped;

import dagger.Component;

@FragmentScoped
@Component(dependencies = RecipesRepositoryComponent.class, modules = RecipesPresenterModule.class)
public interface RecipesComponent {
	
    void inject(RecipesActivity activity);

}
