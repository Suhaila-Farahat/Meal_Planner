package com.example.mealplanner.mealDetails.view;

import com.example.mealplanner.models.mealModel.MealDetails;

public interface MealDetailsView {
    void showMealDetails(MealDetails mealDetails);
    void showError(String message);
    void showLoading();
    void hideLoading();
//    void updateFavoritesStatus(boolean isFavorite);
}

