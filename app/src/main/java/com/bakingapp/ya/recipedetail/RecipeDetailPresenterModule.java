package com.bakingapp.ya.recipedetail;

import com.bakingapp.ya.recipes.RecipesContract;

import dagger.Module;
import dagger.Provides;


@Module
public class RecipeDetailPresenterModule {

    private final RecipeDetailContract.View mView;

    public RecipeDetailPresenterModule(RecipeDetailContract.View view) {
        mView = view;
    }

    @Provides
    RecipeDetailContract.View provideRecipesContractView() {
        return mView;
    }

}
