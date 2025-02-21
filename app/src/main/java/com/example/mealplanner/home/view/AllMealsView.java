package com.example.mealplanner.home.view;
import com.example.mealplanner.models.flagsModel.CountryFlag;
import com.example.mealplanner.models.mealModel.MealCategory;
import com.example.mealplanner.models.mealModel.Meal;
import java.util.List;

public interface AllMealsView {
    void showMeal(List<Meal> meals);
    void showCategories(List<MealCategory> categories);
    void showFlags(List<CountryFlag> flags);
    void showError(String message);
}
