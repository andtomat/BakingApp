
package com.bakingapp.ya.recipedetail.masterdetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bakingapp.ya.recipedetail.RecipeDetailContract;
import com.bakingapp.ya.recipedetail.RecipeDetailPresenter;
import com.bakingapp.ya.recipestepdetail.RecipeStepDetailContract;
import com.bakingapp.ya.recipestepdetail.RecipeStepDetailPresenter;
import com.bakingapp.ya.util.Device;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class RecipeDetailTabletPresenter implements RecipeDetailContract.Presenter, RecipeStepDetailContract.Presenter {

    private RecipeDetailPresenter mDetailPresenter;

    private RecipeStepDetailPresenter mStepDetailPresenter;

    private RecipeDetailContract.View mDetailView;

    private RecipeStepDetailContract.View mStepDetailView;

    private Toolbar mToolbar;

    private Device typeDevice = Device.TABLET;

    @Inject
    public RecipeDetailTabletPresenter(@NonNull RecipeDetailPresenter detailPresenter,
                                       @NonNull RecipeStepDetailPresenter stepDetailPresenter,
                                       @NonNull RecipeDetailContract.View detailView,
                                       @NonNull RecipeStepDetailContract.View stepDetailView,
                                       @NonNull Toolbar toolbar) {
        mDetailPresenter = checkNotNull(detailPresenter);
        mStepDetailPresenter = checkNotNull(stepDetailPresenter);
        mStepDetailView = checkNotNull(stepDetailView);
        mDetailView = checkNotNull(detailView);
        mToolbar = checkNotNull(toolbar);

    }

    @Inject
    void setupListeners() {
        mStepDetailView.setPresenter(this);
        mDetailView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mDetailView.setToolbarTablet(mToolbar);
    }

    @Override
    public void unsubscribe() {
    }

    @Override
    public Device getDevice() {
        return typeDevice;
    }

    @Override
    public void openStepDetailUi(int position) {
        mStepDetailView.showLayout(position);
    }
}
