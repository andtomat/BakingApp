package com.bakingapp.ya;

import com.bakingapp.ya.data.RecipesRepository;
import com.bakingapp.ya.data.model.Recipe;
import com.bakingapp.ya.data.model.Step;
import com.bakingapp.ya.recipes.RecipesContract;
import com.bakingapp.ya.recipes.RecipesPresenter;
import com.bakingapp.ya.util.schedulers.BaseSchedulerProvider;
import com.bakingapp.ya.util.schedulers.ImmediateSchedulerProvider;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Recipe local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RecipesPresenterTest {

    private static ArrayList<Recipe> RECIPES = new ArrayList<>();

    private static ArrayList<Step> STEPS = new ArrayList<>();

    @Mock
    private RecipesRepository mRepository;

    @Mock
    private RecipesContract.View mView;

    private BaseSchedulerProvider mSchedulerProvider;

    private RecipesPresenter mPresenter;

    @Before
    public void setupTasksPresenter() {
        MockitoAnnotations.initMocks(this);

        mSchedulerProvider = new ImmediateSchedulerProvider();

        mPresenter = new RecipesPresenter(mRepository, mView, mSchedulerProvider);

    }

    @Test
    public void loadRecipesFromServer() {

        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("Brownies");
        RECIPES.add(recipe);

        when(mRepository.getRecipes()).thenReturn(Flowable.just(RECIPES));
        mPresenter.getRecipes();
        verify(mView).hideProgress();
        verify(mView).showList(RECIPES);
    }

    @Test
    public void loadErrorRecipesFromServer() {

        when(mRepository.getRecipes()).thenReturn(Flowable.just(RECIPES));
        mPresenter.getRecipes();
        verify(mView).hideProgress();
        verify(mView).showNotFound();
    }

}