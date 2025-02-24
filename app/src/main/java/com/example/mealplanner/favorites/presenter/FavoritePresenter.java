package com.example.mealplanner.favorites.presenter;

import com.example.mealplanner.database.FavoriteMeal;

public interface FavoritePresenter {
    void loadFavorites();
    void toggleFavorite(FavoriteMeal meal);
    void checkIfFavorite(String mealId);
    void onDestroy();
}
