
package com.bakingapp.ya.recipedetail.masterdetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.bakingapp.ya.R;
import com.bakingapp.ya.data.RecipesRepositoryComponent;
import com.bakingapp.ya.data.model.Recipe;
import com.bakingapp.ya.recipedetail.DaggerRecipeDetailComponent;
import com.bakingapp.ya.recipedetail.RecipeDetailActivity;
import com.bakingapp.ya.recipedetail.RecipeDetailComponent;
import com.bakingapp.ya.recipedetail.RecipeDetailFragment;
import com.bakingapp.ya.recipedetail.RecipeDetailPresenterModule;
import com.bakingapp.ya.recipestepdetail.RecipeStepDetailFragment;
import com.bakingapp.ya.util.ActivityUtils;

import javax.inject.Inject;

public class RecipeDetailMvpController {

    private final RecipeDetailActivity mActivity;

    private final RecipesRepositoryComponent mRepositoryComponent;

    private final Toolbar mToolbar;

    @Inject
    RecipeDetailTabletPresenter mTabletPresenter;

    Recipe mRecipe;

    public RecipeDetailMvpController(
            @NonNull RecipeDetailActivity activity,
            @Nullable Recipe recipe,
            @Nullable RecipesRepositoryComponent repositoryComponent,
            @Nullable Toolbar toolbar) {
        mActivity = activity;
        mRecipe = recipe;
        mRepositoryComponent = repositoryComponent;
        mToolbar = toolbar;

        initTasksView();
    }

    private void initTasksView() {
        if (ActivityUtils.isTablet(mActivity)) {
            createTabletElements();
            Log.e("laod", "tablet");
        } else {
            createPhoneElements();
            Log.e("laod", "phone");
        }
    }

    private void createPhoneElements() {

        RecipeDetailFragment mFragment =
                (RecipeDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mFragment == null) {
            mFragment = RecipeDetailFragment.newInstance(mRecipe);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mFragment, R.id.contentFrame);
        }

        DaggerRecipeDetailComponent.builder()
                .recipesRepositoryComponent(mRepositoryComponent)
                .recipeDetailPresenterModule(new RecipeDetailPresenterModule(mFragment)).build()
                .inject(mActivity);
    }


    private void createTabletElements() {

        RecipeDetailFragment detailFragment =
                (RecipeDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (detailFragment == null) {
            detailFragment = RecipeDetailFragment.newInstance(mRecipe);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), detailFragment, R.id.contentFrameList);
        }

        RecipeStepDetailFragment stepDetailFragment =
                (RecipeStepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (stepDetailFragment == null) {
            stepDetailFragment = RecipeStepDetailFragment.newInstance(mRecipe, 0);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), stepDetailFragment, R.id.contentFrameDetail);
        }

        DaggerRecipeDetailTabletComponent.builder()
                .recipesRepositoryComponent(mRepositoryComponent)
                .recipeDetailTabletPresenterModule(new RecipeDetailTabletPresenterModule(
                        detailFragment,
                        stepDetailFragment,
                        mToolbar)).build()
                .inject(this);

    }

    private FragmentManager getSupportFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }
}
