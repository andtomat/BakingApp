package com.bakingapp.ya.recipedetail.masterdetail;

import com.bakingapp.ya.data.RecipesRepositoryComponent;
import com.bakingapp.ya.util.FragmentScoped;

import dagger.Component;

@FragmentScoped
@Component(dependencies = RecipesRepositoryComponent.class, modules = RecipeDetailTabletPresenterModule.class)
public interface RecipeDetailTabletComponent {

    void inject(RecipeDetailMvpController controller);

}
