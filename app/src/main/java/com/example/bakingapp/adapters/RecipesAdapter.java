package com.example.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Taha on 6/13/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private Context mContext;
    private List<Recipe> mRecipeList;
    private RecipeListClickListener mClickListener;

    public RecipesAdapter(Context mContext, List<Recipe> mRecipeList, RecipeListClickListener mClickListener) {
        this.mContext = mContext;
        this.mRecipeList = mRecipeList;
        this.mClickListener = mClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipes_view_item, parent, false);
        return new RecipeViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        if (mRecipeList != null && mRecipeList.size() > 0) {
            //Recipe object
            Recipe recipe = mRecipeList.get(position);
            //Recipe image(if found)
            String imagePath = recipe.getImage();
            if (imagePath != null && imagePath != "") {

                Picasso.with(mContext)
                        .load(recipe.getImage())
                        .error(R.drawable.food)
                        .placeholder(R.drawable.food)
                        .into(holder.recipeImageView);
            }
            //Recipe name and servings
            String name = recipe.getName();
            int servings = recipe.getServings();

            holder.recipeTextView.setText(mContext.getString(R.string.recipe_name, name, servings));
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeList != null ? mRecipeList.size() : 0;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_image)
        ImageView recipeImageView;

        @BindView(R.id.recipe_name)
        TextView recipeTextView;

        private RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickListener.onRecipeCLicked(position);
        }
    }

    public void swapList(List<Recipe> recipeList) {
        if (recipeList != null) {
            mRecipeList = recipeList;
            notifyDataSetChanged();
        }
    }

    //Click listener interface
    public interface RecipeListClickListener {
        void onRecipeCLicked(int position);
    }

}
