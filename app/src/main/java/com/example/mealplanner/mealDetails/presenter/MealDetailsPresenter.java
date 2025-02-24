package com.example.mealplanner.mealDetails.presenter;


public interface MealDetailsPresenter {
    void fetchMealDetails(String mealId);
    void onDestroy();

}

