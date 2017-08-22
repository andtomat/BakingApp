package com.bakingapp.ya.recipes;

import com.bakingapp.ya.BasePresenter;
import com.bakingapp.ya.BaseView;
import com.bakingapp.ya.data.model.Recipe;

import java.util.ArrayList;

public interface RecipesContract {

    interface View extends BaseView<Presenter> {

        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void showList(ArrayList<Recipe> list);

        void showNotFound();

    }

    interface Presenter extends BasePresenter {
        void getRecipes();

        void getRecipes(String keyword);

        void refreshRecipes();
    }
}
