package com.bakingapp.ya.recipedetail.masterdetail;

import android.support.v7.widget.Toolbar;

import com.bakingapp.ya.recipedetail.RecipeDetailContract;
import com.bakingapp.ya.recipestepdetail.RecipeStepDetailContract;

import dagger.Module;
import dagger.Provides;


@Module
public class RecipeDetailTabletPresenterModule {

    private final RecipeDetailContract.View mDetailView;

    private final RecipeStepDetailContract.View mStepDetailView;

    private final Toolbar mToolbar;

    public RecipeDetailTabletPresenterModule(RecipeDetailContract.View detailView,
                                             RecipeStepDetailContract.View stepDetailView,
                                             Toolbar toolbar) {
        mDetailView = detailView;
        mStepDetailView = stepDetailView;
        mToolbar = toolbar;
    }


    @Provides
    Toolbar provideToolbar() {
        return mToolbar;
    }

    @Provides
    RecipeDetailContract.View provideDetailView() {
        return mDetailView;
    }

    @Provides
    RecipeStepDetailContract.View provideStepDetailView() {
        return mStepDetailView;
    }
}
