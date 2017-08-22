package com.bakingapp.ya.recipes;

import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bakingapp.ya.BakingApplication;
import com.bakingapp.ya.R;
import com.bakingapp.ya.util.ActivityUtils;
import com.bakingapp.ya.util.EspressoIdlingResource;

import javax.inject.Inject;

public class RecipesActivity extends AppCompatActivity {

    @Inject
    RecipesPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        RecipesFragment mFragment =
                (RecipesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mFragment == null) {
            mFragment = RecipesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mFragment, R.id.contentFrame);
        }

        DaggerRecipesComponent.builder()
                .recipesRepositoryComponent(((BakingApplication) getApplication()).getRecipesRepositoryComponent())
                .recipesPresenterModule(new RecipesPresenterModule(mFragment)).build()
                .inject(this);
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
