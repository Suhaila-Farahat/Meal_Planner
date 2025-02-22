package com.example.mealplanner.categories.view;

import com.example.mealplanner.models.mealModel.Meal;
import java.util.List;

public interface MealListView {
    void showMeals(List<Meal> meals);
    void showError(String message);
    void showLoading();
    void hideLoading();
}
