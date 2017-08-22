package com.bakingapp.ya.data.source.remote;

import com.bakingapp.ya.data.model.Recipe;

import java.util.ArrayList;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RecipeService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Flowable<ArrayList<Recipe>> getRecipes();

}
