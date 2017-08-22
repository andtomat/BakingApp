package com.bakingapp.ya.recipes;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bakingapp.ya.data.RecipesRepository;
import com.bakingapp.ya.util.EspressoIdlingResource;
import com.bakingapp.ya.util.schedulers.BaseSchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;


public class RecipesPresenter implements RecipesContract.Presenter{

    @NonNull
    private RecipesRepository mRepository;

    @NonNull
    private RecipesContract.View mView;

    @NonNull
    private BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mDisposable;

    @Inject
    public RecipesPresenter(@NonNull RecipesRepository repository,
                     @NonNull RecipesContract.View view,
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
    public void getRecipes() {

        EspressoIdlingResource.increment();

        Disposable disposable = mRepository.getRecipes()
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(data -> {
                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                        EspressoIdlingResource.decrement();
                    }
                    mView.hideProgress();

                    if(data.size()>0){
                        mView.showList(data);
                    }
                    else{
                        mView.showNotFound();
                    }

                }, error -> {
                    mView.hideProgress();
                    mView.showMessage("No Internet Connection");
                });

        mDisposable.add(disposable);
    }

    @Override
    public void getRecipes(String keyword) {
        Disposable disposable = mRepository.getRecipes(keyword)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(data -> {
                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                        EspressoIdlingResource.decrement();
                    }
                    mView.hideProgress();
                    mView.showList(data);
                }, error -> {
                    mView.hideProgress();
                    mView.showMessage("No Internet Connection");
                });

        mDisposable.add(disposable);
    }

    @Override
    public void refreshRecipes() {
        mRepository.refreshRecipes();
    }
}
