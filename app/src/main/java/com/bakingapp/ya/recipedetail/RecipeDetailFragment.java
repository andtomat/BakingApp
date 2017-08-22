package com.bakingapp.ya.recipedetail;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bakingapp.ya.R;
import com.bakingapp.ya.data.model.Recipe;
import com.bakingapp.ya.recipestepdetail.RecipeStepDetailActivity;
import com.bakingapp.ya.widget.RecipeAppWidgetService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements RecipeDetailContract.View {


    private Toolbar mToolbar;

    @BindView(R.id.tv_name)
    TextView mTvName;

    @BindView(R.id.tv_serve)
    TextView mTvServe;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.rv_ingredients)
    RecyclerView mRvIngredients;

    @BindView(R.id.rv_steps)
    RecyclerView mRvSteps;

    @BindView(R.id.ivBackdrop)
    ImageView ivBackdrop;

    @BindView(R.id.iv_widget)
    ImageView ivWidget;

    @BindView(R.id.iv_share)
    ImageView ivShare;

    private RecipeDetailContract.Presenter mPresenter;

    private Recipe mDetailRecipe;

    public RecipeDetailFragment() {

    }

    public static RecipeDetailFragment newInstance(Recipe recipe) {
        RecipeDetailFragment mFragment = new RecipeDetailFragment();
        Bundle mArguments = new Bundle();
        if(recipe!=null){
            mArguments.putParcelable("recipe", recipe);
        }
        mFragment.setArguments(mArguments);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.subscribe();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, root);

        if(root.findViewById(R.id.toolbar)!=null){
            mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDetailRecipe = getArguments().getParcelable("recipe");

        mCollapsingToolbar.setTitle(mDetailRecipe.getName().toUpperCase());
        mCollapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(getActivity(), R.color.color_text));
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        mTvName.setText(mDetailRecipe.getName().toUpperCase());
        mTvServe.setText(String.valueOf(mDetailRecipe.getServings()));


        mRvIngredients.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ListIngredientsAdapter mIngredientAdapter = new ListIngredientsAdapter(new ArrayList<>());
        mRvIngredients.setAdapter(mIngredientAdapter);
        mRvIngredients.setNestedScrollingEnabled(false);
        mRvIngredients.post(() -> mIngredientAdapter.replaceData(mDetailRecipe.getIngredients()));

        mRvSteps.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ListStepsAdapter mStepAdapter = new ListStepsAdapter(new ArrayList<>(), mPresenter.getDevice(), mItemListener);
        mRvSteps.setAdapter(mStepAdapter);
        mRvSteps.setNestedScrollingEnabled(false);
        mRvSteps.post(() -> mStepAdapter.replaceData(mDetailRecipe.getSteps()));

        ivWidget.setOnClickListener(view -> {
            RecipeAppWidgetService.startActionUpdateRecipeWidgets(getActivity(), mDetailRecipe);
            Snackbar snackbar = Snackbar
                    .make(ivWidget, "Widget has been added", Snackbar.LENGTH_SHORT);
            snackbar.show();
        });

        ivShare.setOnClickListener(view -> {
            Uri bmpUri = getLocalBitmapUri(ivBackdrop);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, mDetailRecipe.getName());
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share this recipe"));
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mCollapsingToolbar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.exo_player_height);

        ivBackdrop.getLayoutParams().height = (int) getResources().getDimension(R.dimen.exo_player_height);

    }


    @Override
    public void setPresenter(RecipeDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setToolbarTablet(Toolbar toolbar) {
        mToolbar = toolbar;
    }

    @Override
    public void navigateToStepDetailUi(int position) {
        Intent i  = new Intent(getActivity(), RecipeStepDetailActivity.class);
        i.putExtra("position", position);
        i.putExtra("recipe", mDetailRecipe);
        startActivity(i);
    }

    public interface StepItemListener {
        void onMovieClick(int position);
    }

    StepItemListener mItemListener = position -> {
        mPresenter.openStepDetailUi(position);
    };


    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }

        Uri bmpUri = null;
        try {
            File file =  new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
