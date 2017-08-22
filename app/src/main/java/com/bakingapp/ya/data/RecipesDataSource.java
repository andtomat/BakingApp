
package com.bakingapp.ya.data;

import com.bakingapp.ya.data.model.Recipe;

import java.util.ArrayList;

import io.reactivex.Flowable;

public interface RecipesDataSource {

    Flowable<ArrayList<Recipe>> getRecipes();

    Flowable<ArrayList<Recipe>> getRecipes(String keyword);

    void refreshRecipes();

}
