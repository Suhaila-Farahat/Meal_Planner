package com.example.mealplanner.favorites.view;

import com.example.mealplanner.database.FavoriteMeal;
import java.util.List;

public interface FavoritesView {
    void showFavorites(List<FavoriteMeal> meals);
    void showError(String message);
    void showFavoriteAdded(FavoriteMeal meal);
    void showFavoriteRemoved(FavoriteMeal meal);
    void onFavoriteChecked(boolean isFavorite);
}
