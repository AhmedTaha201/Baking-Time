package com.example.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Taha on 6/16/2018.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.RecipeViewHolder> {

    Context mContext;
    private List<Recipe.Step> mStepList;
    private StepItemClickListener mClickListener;

    public StepAdapter(Context mContext, List<Recipe.Step> mStepList, StepItemClickListener mClickListener) {
        this.mContext = mContext;
        this.mStepList = mStepList;
        this.mClickListener = mClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_view_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        if (mStepList != null){
            Recipe.Step step = mStepList.get(position);
            holder.stepDescriptionView.setText(step.getShortDescription());
            holder.steDescriptionLongView.setText(step.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return mStepList != null ? mStepList.size() : 0;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_description)
        TextView stepDescriptionView;

        @BindView(R.id.step_description_long)
        TextView steDescriptionLongView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onStepClick(getAdapterPosition());
        }
    }

    public interface StepItemClickListener{
        void onStepClick(int position);
    }
}
