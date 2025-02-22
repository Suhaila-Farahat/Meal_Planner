package com.example.mealplanner.models.mealModel;

import java.util.ArrayList;
import java.util.List;

public class MealDetails {
    private String strMeal;
    private String strMealThumb;
    private String strArea;
    private String strInstructions;
    private String strYoutube;

    private String strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5;
    private String strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5;

    public List<Ingredient> getIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        addIngredient(ingredients, strIngredient1, strMeasure1);
        addIngredient(ingredients, strIngredient2, strMeasure2);
        addIngredient(ingredients, strIngredient3, strMeasure3);
        addIngredient(ingredients, strIngredient4, strMeasure4);
        addIngredient(ingredients, strIngredient5, strMeasure5);
        return ingredients;
    }

    private void addIngredient(List<Ingredient> list, String ingredient, String measurement) {
        if (ingredient != null && !ingredient.isEmpty()) {
            String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredient + "-Small.png";
            list.add(new Ingredient(ingredient, imageUrl, measurement));
        }
    }

    public String getStrMeal() { return strMeal; }
    public String getStrMealThumb() { return strMealThumb; }
    public String getStrArea() { return strArea; }
    public String getStrInstructions() { return strInstructions; }
    public String getStrYoutube() { return strYoutube; }
}
