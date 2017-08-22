package com.bakingapp.ya.data;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.bakingapp.ya.data.model.Recipe;
import com.bakingapp.ya.data.source.local.Local;
import com.bakingapp.ya.data.source.remote.Remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

@Singleton
public class RecipesRepository implements RecipesDataSource{

    private final RecipesDataSource mRecipesRemoteDataSource;

    private final RecipesDataSource mMoviesLocalDataSource;

    @VisibleForTesting
    @Nullable
    Map<String, Recipe> mCachedRecipes;

    @VisibleForTesting
    boolean mRecipeCacheIsDirty = false;

    @Inject
    RecipesRepository(@Local RecipesDataSource recipesLocalDataSource, @Remote RecipesDataSource recipesRemoteDataSource) {
        mRecipesRemoteDataSource = recipesLocalDataSource;
        mMoviesLocalDataSource =  recipesRemoteDataSource;
    }

    @Override
    public Flowable<ArrayList<Recipe>> getRecipes() {
        if (mCachedRecipes != null && !mRecipeCacheIsDirty) {
            return Flowable.just(convertHashToArraylist(mCachedRecipes.values()));

        } else if (mCachedRecipes == null) {
            mCachedRecipes = new LinkedHashMap<>();
        }

        return mRecipesRemoteDataSource.getRecipes()
                .flatMap(recipeeResults -> {
                    Flowable.fromIterable(recipeeResults)
                            .doOnNext(recipe -> mCachedRecipes.put(String.valueOf(recipe.getId()), recipe)).subscribe();
                    return Flowable.just(recipeeResults);
                })
                .doOnComplete(() -> mRecipeCacheIsDirty = false);

    }

    @Override
    public Flowable<ArrayList<Recipe>> getRecipes(String keyword) {
        ArrayList<Recipe> mListResult = new ArrayList<>();
        Flowable.fromIterable(convertHashToArraylist(mCachedRecipes.values()))
                .filter(recipe -> recipe.getName().toLowerCase().contains(keyword.toLowerCase()))
                .doOnNext(recipe -> mListResult.add(recipe)).subscribe();
        return Flowable.just(mListResult);
    }

    @Override
    public void refreshRecipes() {
        mRecipeCacheIsDirty = true;
    }

    public ArrayList<Recipe> convertHashToArraylist(Collection<Recipe> collection){
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        recipeArrayList.addAll(collection);
        return recipeArrayList;
    }
}
