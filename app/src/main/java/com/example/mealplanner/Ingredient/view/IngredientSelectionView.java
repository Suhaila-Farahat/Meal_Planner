package com.example.mealplanner.Ingredient.view;


import com.example.mealplanner.models.mealModel.Ingredient;
import java.util.List;

public interface IngredientSelectionView {
    void showIngredients(List<Ingredient> ingredients);
    void showError(String message);
}
