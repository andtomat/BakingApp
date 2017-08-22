package com.bakingapp.ya.recipedetail;

import com.bakingapp.ya.data.RecipesRepositoryComponent;
import com.bakingapp.ya.util.FragmentScoped;

import dagger.Component;

@FragmentScoped
@Component(dependencies = RecipesRepositoryComponent.class, modules = RecipeDetailPresenterModule.class)
public interface RecipeDetailComponent {
	
    void inject(RecipeDetailActivity activity);

}
