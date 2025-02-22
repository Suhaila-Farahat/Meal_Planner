package com.example.mealplanner.countries.view;


import com.example.mealplanner.models.mealModel.Meal;
import java.util.List;

public interface CountryMealListView {
    void showMeals(List<Meal> meals);
    void showError(String message);
}
