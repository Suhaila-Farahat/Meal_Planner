package com.example.mealplanner.models;

import java.util.List;


import com.example.mealplanner.models.mealModel.Meal;
import com.example.mealplanner.models.mealModel.MealCategory;
import com.google.gson.annotations.SerializedName;

public class NetworkResponse {
    @SerializedName("meals")
    private List<Meal> meals;

    @SerializedName("categories")
    private List<MealCategory> categories;

    public List<Meal> getMeals() {

        return meals;
    }

    public List<MealCategory> getCategories() {

        return categories;
    }
}






