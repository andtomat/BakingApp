package com.bakingapp.ya.recipes;

import dagger.Module;
import dagger.Provides;


@Module
public class RecipesPresenterModule {

    private final RecipesContract.View mView;

    public RecipesPresenterModule(RecipesContract.View view) {
        mView = view;
    }

    @Provides
    RecipesContract.View provideRecipesContractView() {
        return mView;
    }

}
