package com.bakingapp.ya.recipedetail;

import android.support.annotation.NonNull;

import com.bakingapp.ya.data.RecipesRepository;
import com.bakingapp.ya.recipes.RecipesContract;
import com.bakingapp.ya.util.Device;
import com.bakingapp.ya.util.schedulers.BaseSchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;


public class RecipeDetailPresenter implements RecipeDetailContract.Presenter{

    @NonNull
    private RecipesRepository mRepository;

    @NonNull
    private RecipeDetailContract.View mView;

    @NonNull
    private BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mDisposable;

    private Device typeDevice = Device.PHONE;

    @Inject
    RecipeDetailPresenter(@NonNull RecipesRepository repository,
                          @NonNull RecipeDetailContract.View view,
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

    @Override
    public void openStepDetailUi(int position) {
        mView.navigateToStepDetailUi(position);
    }
}
