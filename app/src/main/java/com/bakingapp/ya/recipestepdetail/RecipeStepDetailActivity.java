package com.bakingapp.ya.recipestepdetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bakingapp.ya.BakingApplication;
import com.bakingapp.ya.R;
import com.bakingapp.ya.util.ActivityUtils;

import javax.inject.Inject;

public class RecipeStepDetailActivity extends AppCompatActivity {

    @Inject
    RecipeStepDetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        RecipeStepDetailFragment mFragment =
                (RecipeStepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mFragment == null) {
            mFragment = RecipeStepDetailFragment.newInstance(getIntent().getExtras().getParcelable("recipe"), getIntent().getIntExtra("position" , 0));
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mFragment, R.id.contentFrame);
        }

        DaggerRecipeStepDetailComponent.builder()
                .recipesRepositoryComponent(((BakingApplication) getApplication()).getRecipesRepositoryComponent())
                .recipeStepDetailPresenterModule(new RecipeStepDetailPresenterModule(mFragment)).build()
                .inject(this);
    }
}
