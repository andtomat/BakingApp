package com.bakingapp.ya.recipedetail;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.bakingapp.ya.BakingApplication;
import com.bakingapp.ya.R;
import com.bakingapp.ya.data.model.Recipe;
import com.bakingapp.ya.recipedetail.masterdetail.RecipeDetailMvpController;
import com.bakingapp.ya.util.ActivityUtils;

import javax.inject.Inject;

public class RecipeDetailActivity extends AppCompatActivity {

    @Inject
    RecipeDetailPresenter mPresenter;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Recipe recipe = getIntent().getExtras().getParcelable("recipe");

        if(findViewById(R.id.toolbar)!=null){
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(recipe.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        new RecipeDetailMvpController(this, recipe, ((BakingApplication) getApplication()).getRecipesRepositoryComponent(), mToolbar);

        setResult(Activity.RESULT_OK, createResultData(recipe.getName()));

    }

    @VisibleForTesting
    public static Intent createResultData(String data) {
        final Intent resultData = new Intent();
        resultData.putExtra("recipe_name", data);
        return resultData;
    }
}
