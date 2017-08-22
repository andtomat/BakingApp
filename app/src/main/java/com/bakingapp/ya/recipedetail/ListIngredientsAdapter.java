package com.bakingapp.ya.recipedetail;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bakingapp.ya.R;
import com.bakingapp.ya.data.model.Ingredient;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListIngredientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Ingredient> mList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient)
        TextView mTvIngredient;

        Ingredient currentData;

        public ViewHolder(View v) {
            super(v);

            ButterKnife.bind(this, v);
        }
    }

    public ListIngredientsAdapter(ArrayList<Ingredient> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) != null ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ViewHolder) holder).currentData = mList.get(position);
        NumberFormat format = new DecimalFormat("0.#");
        String ingredient = format.format(mList.get(position).getQuantity()) + " " + mList.get(position).getMeasure() + "  <b>" + mList.get(position).getIngredient() + "</b>";
        ((ViewHolder) holder).mTvIngredient.setText(Html.fromHtml(ingredient));
    }

    @Override
    public int getItemCount() {
        return (mList != null) ? mList.size(): 0;
    }

    public void replaceData(ArrayList<Ingredient> list) {
        mList = list;
        notifyDataSetChanged();
    }

}