package com.bakingapp.ya.recipestepdetail;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bakingapp.ya.R;
import com.bakingapp.ya.data.model.Recipe;
import com.bakingapp.ya.util.Device;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailFragment extends Fragment implements RecipeStepDetailContract.View {

    private Toolbar mToolbar;

    @BindView(R.id.tv_step)
    TextView mTvStep;

    @BindView(R.id.ll_next)
    LinearLayout llNext;

    @BindView(R.id.ll_back)
    LinearLayout llBack;

    @BindView(R.id.ll_navigation)
    LinearLayout llNavigation;

    @BindView(R.id.img_background_video)
    ImageView mImgBackVideo;

    @BindView(R.id.sep_step_video)
    SimpleExoPlayerView mSimpleExoPlayerView;

    private RecipeStepDetailContract.Presenter mPresenter;

    private Recipe mRecipe;

    private int mPosition;

    private View root;

    private SimpleExoPlayer player;

    public RecipeStepDetailFragment() {

    }

    public static RecipeStepDetailFragment newInstance(Recipe recipe, int position) {
        RecipeStepDetailFragment mFragment = new RecipeStepDetailFragment();
        Bundle mArguments = new Bundle();
        if(recipe!=null){
            mArguments.putParcelable("recipe", recipe);
            mArguments.putInt("position", position);
        }
        mFragment.setArguments(mArguments);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        ButterKnife.bind(this, root);

        mPresenter.subscribe();

        mSimpleExoPlayerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.exo_player_height);
        mSimpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        if(getArguments().containsKey("recipe")){
            mRecipe = getArguments().getParcelable("recipe");
            mPosition = getArguments().getInt("position");
        }

        if(root.findViewById(R.id.toolbar)!=null){
            mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mRecipe.getName());

            mToolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.color_text));

            llNext.setOnClickListener(view -> {
                mPosition++;
                initPlayerAndNavigation();
            });

            llBack.setOnClickListener(view -> {
                mPosition--;
                initPlayerAndNavigation();
            });

        }

        return root;
    }

    public void initPlayerAndNavigation(){
        if(mPresenter.getDevice() == Device.PHONE){
            if(mPosition == 0){
                llBack.setVisibility(View.GONE);
            }
            else{
                llBack.setVisibility(View.VISIBLE);
            }
            if(mPosition == mRecipe.getSteps().size()-1){
                llNext.setVisibility(View.GONE);
            }
            else{
                llNext.setVisibility(View.VISIBLE);
            }
        }
        else{
            llNavigation.setVisibility(View.GONE);
        }

        mTvStep.setText(mRecipe.getSteps().get(mPosition).getDescription());

        mSimpleExoPlayerView.setVisibility(View.VISIBLE);
        mImgBackVideo.setVisibility(View.VISIBLE);

        TrackSelector trackSelector =
                new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);

        mSimpleExoPlayerView.setPlayer(player);

        if(!mRecipe.getSteps().get(mPosition).getVideoURL().isEmpty()){
            MediaSource videoSource = new ExtractorMediaSource(Uri.parse(mRecipe.getSteps().get(mPosition).getVideoURL()), new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getActivity(), "Baking App")), new DefaultExtractorsFactory(), null, null);
            player.prepare(videoSource);
            player.setPlayWhenReady(true);
            if(!mRecipe.getSteps().get(mPosition).getThumbnailURL().isEmpty()){
                Picasso.with(getActivity()).load(mRecipe.getSteps().get(mPosition).getThumbnailURL()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mSimpleExoPlayerView.setDefaultArtwork(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        }
        else{
            mSimpleExoPlayerView.setVisibility(View.GONE);
            if(!mRecipe.getSteps().get(mPosition).getThumbnailURL().isEmpty()){
                if(mRecipe.getSteps().get(mPosition).getThumbnailURL().contains("mp4")){
                    mSimpleExoPlayerView.setVisibility(View.VISIBLE);
                    MediaSource videoSource = new ExtractorMediaSource(Uri.parse(mRecipe.getSteps().get(mPosition).getThumbnailURL()), new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getActivity(), "Baking App")), new DefaultExtractorsFactory(), null, null);
                    player.prepare(videoSource);
                    player.setPlayWhenReady(true);
                }
                else{
                    Picasso.with(getActivity()).load(mRecipe.getSteps().get(mPosition).getThumbnailURL()).into(mImgBackVideo);
                }
            }
            else{
                mImgBackVideo.setVisibility(View.GONE);
            }
        }
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
    public void onResume() {
        super.onResume();
        if(player==null){
            initPlayerAndNavigation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player!=null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player!=null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
        if (player!=null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void setPresenter(RecipeStepDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && mPresenter.getDevice() == Device.PHONE){
            mToolbar.setVisibility(View.GONE);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            mSimpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            mSimpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        else if(mPresenter.getDevice() == Device.PHONE){
            mToolbar.setVisibility(View.VISIBLE);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mSimpleExoPlayerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.exo_player_height);
            mSimpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        else{
            mSimpleExoPlayerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.exo_player_height);
            mSimpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

    @Override
    public void showLayout(int position) {

        mPosition = position;

        initPlayerAndNavigation();


    }
}
