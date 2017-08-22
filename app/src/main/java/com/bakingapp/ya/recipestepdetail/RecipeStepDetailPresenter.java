package com.bakingapp.ya.recipestepdetail;

import android.support.annotation.NonNull;

import com.bakingapp.ya.data.RecipesRepository;
import com.bakingapp.ya.recipedetail.RecipeDetailContract;
import com.bakingapp.ya.util.Device;
import com.bakingapp.ya.util.schedulers.BaseSchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

import static com.google.common.base.Preconditions.checkNotNull;


public class RecipeStepDetailPresenter implements RecipeStepDetailContract.Presenter{

    @NonNull
    private RecipesRepository mRepository;

    @NonNull
    private RecipeStepDetailContract.View mView;

    @NonNull
    private BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mDisposable;

    private Device typeDevice = Device.PHONE;

    @Inject
    RecipeStepDetailPresenter(@NonNull RecipesRepository repository,
                              @NonNull RecipeStepDetailContract.View view,
                              @NonNull BaseSchedulerProvider schedulerProvider) {

        mRepository = checkNotNull(repository, "cannot be null!");
        mView = checkNotNull(view, "cannot be null!");
        mSchedulerProvider = checkNotNull(schedulerProvider, "cannot be null!");

        mDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mDisposable.clear();
    }

    @Override
    public Device getDevice() {
        return typeDevice;
    }
}
