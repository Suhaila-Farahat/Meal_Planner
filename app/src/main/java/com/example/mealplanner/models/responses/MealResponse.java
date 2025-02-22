package com.example.mealplanner.models.responses;


import java.util.List;
import com.example.mealplanner.models.mealModel.Meal;
import com.google.gson.annotations.SerializedName;

public class MealResponse {

    @SerializedName("meals")
    private List<Meal> meals;

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}

