package com.example.mealplanner.favorites.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.database.FavoriteMeal;
import com.example.mealplanner.favorites.presenter.FavoritePresenter;
import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {
    private List<FavoriteMeal> favoriteMeals = new ArrayList<>();
    private FavoritePresenter presenter;

    public FavoritesAdapter(FavoritePresenter presenter) {
        this.presenter = presenter;
    }

    public void setFavorites(List<FavoriteMeal> meals) {
        this.favoriteMeals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_meal, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteMeal meal = favoriteMeals.get(position);
        holder.mealName.setText(meal.getName());

        Glide.with(holder.itemView.getContext())
                .load(meal.getImageUrl())
                .into(holder.mealImage);

        holder.favButton.setImageResource(R.drawable.ic_favorite);

        holder.favButton.setOnClickListener(v -> {

            presenter.toggleFavorite(meal);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteMeals.size();
    }


    public void removeItem(int position) {
        if (position >= 0 && position < favoriteMeals.size()) {
            favoriteMeals.remove(position);
            notifyItemRemoved(position);
        }
    }

    public int getMealPosition(String mealId) {
        for (int i = 0; i < favoriteMeals.size(); i++) {
            if (favoriteMeals.get(i).getId().equals(mealId)) {
                return i;
            }
        }
        return -1;
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        ImageView mealImage;
        ImageButton favButton;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.favorite_meal_name);
            mealImage = itemView.findViewById(R.id.favorite_meal_image);
            favButton = itemView.findViewById(R.id.favorite_meal_button);
        }
    }
}