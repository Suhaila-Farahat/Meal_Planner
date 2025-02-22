package com.example.mealplanner.models.responses;

import java.util.List;
import com.example.mealplanner.models.mealModel.MealCategory;
import com.google.gson.annotations.SerializedName;

public class CategoryResponse {

    @SerializedName("categories")
    private List<MealCategory> categories;

    public List<MealCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<MealCategory> categories) {
        this.categories = categories;
    }
}

