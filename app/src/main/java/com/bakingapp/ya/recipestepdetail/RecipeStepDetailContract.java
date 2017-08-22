package com.bakingapp.ya.recipestepdetail;

import com.bakingapp.ya.BasePresenter;
import com.bakingapp.ya.BaseView;
import com.bakingapp.ya.data.model.Recipe;
import com.bakingapp.ya.util.Device;

public interface RecipeStepDetailContract {

    interface View extends BaseView<Presenter> {

        void showLayout(int position);

    }

    interface Presenter extends BasePresenter {

        Device getDevice();

    }
}
