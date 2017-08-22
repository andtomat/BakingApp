package com.bakingapp.ya.recipedetail;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bakingapp.ya.BakingApplication;
import com.bakingapp.ya.R;
import com.bakingapp.ya.data.model.Ingredient;
import com.bakingapp.ya.data.model.Step;
import com.bakingapp.ya.util.Device;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListStepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Step> mList;

    private Device mDevice;

    private int mPositionCLicked = 100000;

    private RecipeDetailFragment.StepItemListener mItemListener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_background_step)
        LinearLayout mLlBackgroundStep;

        @BindView(R.id.tv_step)
        TextView mTvStep;

        Step currentData;

        int mPosition;

        public ViewHolder(View v) {
            super(v);

            ButterKnife.bind(this, v);

            v.setOnClickListener(view -> {
                mPositionCLicked = mPosition;
                notifyDataSetChanged();
                mItemListener.onMovieClick(mPositionCLicked);
            });
        }
    }

    public ListStepsAdapter(ArrayList<Step> list, Device device, RecipeDetailFragment.StepItemListener itemListener) {
        mList = list;
        mDevice = device;
        mItemListener = itemListener;

        if(mDevice==Device.TABLET){
            mPositionCLicked = 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) != null ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ViewHolder) holder).mPosition = position;

        ((ViewHolder) holder).currentData = mList.get(position);
        ((ViewHolder) holder).mTvStep.setText(mList.get(position).getShortDescription());

        if(mDevice==Device.TABLET){
            if(mPositionCLicked == position){
                ((ViewHolder) holder).mLlBackgroundStep.setBackgroundColor(ContextCompat.getColor(BakingApplication.getRecipesRepositoryComponent().getContext(), R.color.ripple_orange));
            }
            else{
                TypedValue outValue = new TypedValue();
                BakingApplication.getRecipesRepositoryComponent().getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                ((ViewHolder) holder).mLlBackgroundStep.setBackgroundResource(outValue.resourceId);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mList != null) ? mList.size(): 0;
    }

    public void replaceData(ArrayList<Step> list) {
        mList = list;
        notifyDataSetChanged();
    }

}