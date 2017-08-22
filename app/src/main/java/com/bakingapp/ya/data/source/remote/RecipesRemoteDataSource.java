package com.bakingapp.ya.data.source.remote;

import android.graphics.Movie;
import android.support.annotation.NonNull;

import com.bakingapp.ya.BakingApplication;
import com.bakingapp.ya.data.RecipesDataSource;
import com.bakingapp.ya.data.model.Recipe;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class RecipesRemoteDataSource implements RecipesDataSource {

    @Inject
    RecipeService mService;

    public RecipesRemoteDataSource(){
        BakingApplication.getRecipesRepositoryComponent().inject(this);
    }

    @Override
    public Flowable<ArrayList<Recipe>> getRecipes() {
        return mService.getRecipes();
    }

    @Override
    public Flowable<ArrayList<Recipe>> getRecipes(String keyword) {
        return null;
    }

    @Override
    public void refreshRecipes() {

    }
}
