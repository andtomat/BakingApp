package com.bakingapp.ya.recipes;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bakingapp.ya.BakingApplication;
import com.bakingapp.ya.R;
import com.bakingapp.ya.data.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListRecipesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Recipe> mList;
    private RecipesFragment.RecipeItemListener mItemListener;

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pbLoading)
        ProgressBar pbLoading;

        public ProgressViewHolder(View v) {
            super(v);

            ButterKnife.bind(this, v);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView mTvName;

        @BindView(R.id.tv_ingredients)
        TextView mTvIngredients;

        @BindView(R.id.tv_serve)
        TextView mTvServe;

        @BindView(R.id.tv_steps)
        TextView mTvSteps;

        @BindView(R.id.img_background)
        ImageView mIvBackground;

        Recipe cerrentData;

        public ViewHolder(View v) {
            super(v);

            ButterKnife.bind(this, v);

            v.setOnClickListener(view -> mItemListener.onMovieClick(cerrentData));
        }
    }

    public ListRecipesAdapter(ArrayList<Recipe> list, RecipesFragment.RecipeItemListener itemListener) {
        mList = list;
        mItemListener = itemListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) != null ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;

        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
            vh = new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).mTvName.setText(mList.get(position).getName());
            ((ViewHolder) holder).mTvServe.setText(String.valueOf(mList.get(position).getServings()));
            ((ViewHolder) holder).mTvIngredients.setText(String.valueOf(mList.get(position).getIngredients().size()));
            ((ViewHolder) holder).mTvSteps.setText(String.valueOf(mList.get(position).getSteps().size()));
            if(!mList.get(position).getImage().equalsIgnoreCase("")){
                Picasso.with(BakingApplication.getRecipesRepositoryComponent().getContext())
                        .load(mList.get(position).getImage())
                        .into(((ViewHolder) holder).mIvBackground);
            }

            ((ViewHolder) holder).cerrentData = mList.get(position);
        }
        else{
            ((ProgressViewHolder) holder).pbLoading.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return (mList != null) ? mList.size(): 0;
    }

    public void replaceData(ArrayList<Recipe> list) {
        mList = list;
        notifyDataSetChanged();
    }

}