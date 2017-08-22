package com.bakingapp.ya.recipestepdetail;

import com.bakingapp.ya.recipedetail.RecipeDetailContract;

import dagger.Module;
import dagger.Provides;


@Module
public class RecipeStepDetailPresenterModule {

    private final RecipeStepDetailContract.View mView;

    public RecipeStepDetailPresenterModule(RecipeStepDetailContract.View view) {
        mView = view;
    }

    @Provides
    RecipeStepDetailContract.View provideRecipesContractView() {
        return mView;
    }
}
