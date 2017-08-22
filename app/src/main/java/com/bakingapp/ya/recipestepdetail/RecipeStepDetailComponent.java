package com.bakingapp.ya.recipestepdetail;

import com.bakingapp.ya.data.RecipesRepositoryComponent;
import com.bakingapp.ya.util.FragmentScoped;

import dagger.Component;

@FragmentScoped
@Component(dependencies = RecipesRepositoryComponent.class, modules = RecipeStepDetailPresenterModule.class)
public interface RecipeStepDetailComponent {
	
    void inject(RecipeStepDetailActivity activity);

}
