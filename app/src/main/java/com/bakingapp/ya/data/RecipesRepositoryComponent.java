package com.bakingapp.ya.data;

import android.content.Context;

import com.bakingapp.ya.ApplicationModule;
import com.bakingapp.ya.data.source.remote.RecipesRemoteDataSource;
import com.bakingapp.ya.util.schedulers.BaseSchedulerProvider;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {RecipesRepositoryModule.class, ApplicationModule.class})
public interface RecipesRepositoryComponent {

    RecipesRepository getRecipesRepository();

    BaseSchedulerProvider getBaseSchedulerProvider();

    Context getContext();

    void inject(RecipesRemoteDataSource moviesRemoteDataSource);

}
