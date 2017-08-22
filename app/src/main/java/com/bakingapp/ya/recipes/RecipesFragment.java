package com.bakingapp.ya.recipes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bakingapp.ya.R;
import com.bakingapp.ya.data.model.Recipe;
import com.bakingapp.ya.recipedetail.RecipeDetailActivity;
import com.bakingapp.ya.widget.RecipeAppWidgetService;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class RecipesFragment extends Fragment implements RecipesContract.View {

    private RecipesContract.Presenter mPresenter;

    @BindView(R.id.rv_recipes)
    RecyclerView mRecyclerView;

    @BindView(R.id.pb_recipes)
    ProgressBar mProgressBarLoading;

    @BindView(R.id.ll_notfound)
    LinearLayout mLinearLayoutNotFound;

    @BindView(R.id.fsv_recipes)
    FloatingSearchView mFloatingSearchView;

    private String mLastQuery = "";

    private ListRecipesAdapter recyclerAdapter;

    private ArrayList<Recipe> mList = new ArrayList<>();

    private GridLayoutManager mLayoutManager;

    public RecipesFragment() {
    }

    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.subscribe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, root);

        recyclerAdapter = new ListRecipesAdapter(new ArrayList<>(), mItemListener);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(recyclerAdapter);

        mLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.movies_columns));
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                int spanCount = mLayoutManager.getSpanCount();
                return mList.get(position) != null ? 1 : spanCount;
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        setHasOptionsMenu(true);

        setupFloatingSearch();

        mPresenter.getRecipes();

        return root;
    }

    private void setupFloatingSearch() {

        mFloatingSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
            mFloatingSearchView.showProgress();
            mPresenter.getRecipes(newQuery);
            mLastQuery = newQuery;
        });

        mFloatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                mPresenter.getRecipes(query);
            }
        });

        mFloatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mFloatingSearchView.setSearchText(mLastQuery);
            }

            @Override
            public void onFocusCleared() {
                mFloatingSearchView.setSearchBarTitle(mLastQuery);
            }
        });

        mFloatingSearchView.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.action_voice_rec) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
                try {
                    startActivityForResult(intent, 123);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getActivity(), "Speech not supported", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initGrid(){
        mLayoutManager.setSpanCount(getResources().getInteger(R.integer.movies_columns));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void showProgress() {
        mProgressBarLoading.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        mProgressBarLoading.setVisibility(View.GONE);
        mLinearLayoutNotFound.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Snackbar snackbar = Snackbar
                .make(mRecyclerView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", view -> {
                    showProgress();
                    mPresenter.refreshRecipes();
                    mPresenter.getRecipes();
                    mLinearLayoutNotFound.setVisibility(View.GONE);
                });
        snackbar.show();
    }

    @Override
    public void showList(ArrayList<Recipe> list) {
        mList = list;
        recyclerAdapter.replaceData(mList);
        mFloatingSearchView.hideProgress();
        mLinearLayoutNotFound.setVisibility(View.GONE);
    }

    @Override
    public void showNotFound() {
        mLinearLayoutNotFound.setVisibility(View.VISIBLE);
        recyclerAdapter.replaceData(new ArrayList<>());
        mFloatingSearchView.hideProgress();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initGrid();
    }


    @Override
    public void setPresenter(RecipesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public interface RecipeItemListener {
        void onMovieClick(Recipe data);
    }

    RecipeItemListener mItemListener = data -> {
        Intent i  = new Intent(getActivity(), RecipeDetailActivity.class);
        i.putExtra("recipe", data);
        startActivity(i);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 123: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mPresenter.getRecipes(result.get(0));
                    mFloatingSearchView.setSearchBarTitle(result.get(0));
                }
                break;
            }
        }
    }
}
