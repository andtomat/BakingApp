package com.bakingapp.ya;

import android.app.Application;

import com.bakingapp.ya.data.DaggerRecipesRepositoryComponent;
import com.bakingapp.ya.data.RecipesRepositoryComponent;
import com.facebook.stetho.Stetho;

public class BakingApplication extends Application {

    private static RecipesRepositoryComponent mRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mRepositoryComponent = DaggerRecipesRepositoryComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();

        Stetho.initializeWithDefaults(this);

    }

    public static RecipesRepositoryComponent getRecipesRepositoryComponent() {
        return mRepositoryComponent;
    }
}
