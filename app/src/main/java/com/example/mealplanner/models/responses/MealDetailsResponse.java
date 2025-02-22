package com.example.mealplanner.models.responses;


import java.util.List;
import com.example.mealplanner.models.mealModel.MealDetails;
import com.google.gson.annotations.SerializedName;

public class MealDetailsResponse {

    @SerializedName("meals")
    private List<MealDetails> mealDetails;

    public List<MealDetails> getMealDetails() {
        return mealDetails;
    }

    public void setMealDetails(List<MealDetails> mealDetails) {
        this.mealDetails = mealDetails;
    }
}

