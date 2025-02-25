package com.example.mealplanner.mealDetails.presenter;

import com.example.mealplanner.mealplanning.model.PlannedMeal;

public interface MealDetailsPresenter {
    void fetchMealDetails(String mealId);
    void scheduleMeal(PlannedMeal plannedMeal);
    void onDestroy();
}
