package com.example.mealplanner.models;

import java.util.List;
import com.example.mealplanner.models.mealModel.Meal;
import com.example.mealplanner.models.mealModel.MealCategory;
import com.example.mealplanner.models.mealModel.MealDetails;
import com.google.gson.annotations.SerializedName;

public class NetworkResponse {

    @SerializedName("meals")
    private List<Meal> meals;

    @SerializedName("categories")
    private List<MealCategory> categories;

    @SerializedName("mealDetails")
    private List<MealDetails> mealDetails;

    public List<Meal> getMeals() {
        return meals;
    }

    public List<MealCategory> getCategories() {
        return categories;
    }

    public List<MealDetails> getMealDetails() {
        return mealDetails;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public void setMealDetails(List<MealDetails> mealDetails) {
        this.mealDetails = mealDetails;
    }
}
