package com.example.mealplanner.models.mealModel;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NetworkResponse {
    @SerializedName("meals")
    private List<Meal> meals;

    public List<Meal> getMeals() {

        return meals;
    }

    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }
}
