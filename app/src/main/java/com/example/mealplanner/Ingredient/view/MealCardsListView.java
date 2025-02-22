package com.example.mealplanner.Ingredient.view;


import com.example.mealplanner.models.mealModel.Meal;
import java.util.List;

public interface MealCardsListView {
    void showMeals(List<Meal> meals);
    void showError(String message);
}

