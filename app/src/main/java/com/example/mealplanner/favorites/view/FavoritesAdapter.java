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
import com.example.mealplanner.database.FavoriteMealDao;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {
    private List<FavoriteMeal> favoriteMeals = new ArrayList<>();
    private FavoriteMealDao favoriteMealDao;
    private CompositeDisposable disposable = new CompositeDisposable();

    public FavoritesAdapter(FavoriteMealDao favoriteMealDao) {
        this.favoriteMealDao = favoriteMealDao;
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
            removeMealFromFavorites(meal, position);
        });
    }

    private void removeMealFromFavorites(FavoriteMeal meal, int position) {
        disposable.add(favoriteMealDao.deleteFavorite(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    favoriteMeals.remove(position);
                    notifyItemRemoved(position);
                }, Throwable::printStackTrace));
    }

    @Override
    public int getItemCount() {
        return favoriteMeals.size();
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
