package com.example.mealplanner.categories.presenter;

public interface MealListPresenter {
    void fetchMeals(String categoryName);
    void onDestroy();
}
