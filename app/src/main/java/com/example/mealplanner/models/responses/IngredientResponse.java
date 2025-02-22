package com.example.mealplanner.models.responses;


import com.example.mealplanner.models.mealModel.Ingredient;

import java.util.List;

public class IngredientResponse {
    private List<Ingredient> meals;

    public List<Ingredient> getIngredients() {
        return meals;
    }
}

