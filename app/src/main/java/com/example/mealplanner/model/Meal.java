package com.example.mealplanner.model;
import com.google.gson.annotations.SerializedName;

public class Meal {
    @SerializedName("strMeal")
    private String name;

    @SerializedName("strMealThumb")
    private String imageUrl;

    public String getName() {

        return name;
    }

    public String getImageUrl() {

        return imageUrl;
    }
}