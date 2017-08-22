package com.bakingapp.ya.data;

import android.content.Context;

import com.bakingapp.ya.data.source.local.Local;
import com.bakingapp.ya.data.source.remote.RecipesRemoteDataSource;
import com.bakingapp.ya.data.source.remote.Remote;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipesRepositoryModule {

    @Singleton
    @Provides
    @Local
    RecipesDataSource provideRecipesLocalDataSource(Context context) {
        return new RecipesRemoteDataSource();
    }

    @Singleton
    @Provides
    @Remote
    RecipesDataSource provideRecipesRemoteDataSource() {
        return new RecipesRemoteDataSource();
    }

}
