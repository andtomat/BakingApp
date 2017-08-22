package com.bakingapp.ya.recipedetail;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bakingapp.ya.BasePresenter;
import com.bakingapp.ya.BaseView;
import com.bakingapp.ya.data.model.Recipe;
import com.bakingapp.ya.util.Device;

public interface RecipeDetailContract {

    interface View extends BaseView<Presenter> {

        void setToolbarTablet(Toolbar toolbar);

        void navigateToStepDetailUi(int position);

    }

    interface Presenter extends BasePresenter {

        Device getDevice();

        void openStepDetailUi(int position);

    }
}
