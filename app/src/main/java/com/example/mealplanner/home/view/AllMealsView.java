package com.example.mealplanner.home.view;
import com.example.mealplanner.model.Meal;
import java.util.List;

public interface AllMealsView {
    void showMeal(List<Meal> mealsList);
    void showError(String message);
}
