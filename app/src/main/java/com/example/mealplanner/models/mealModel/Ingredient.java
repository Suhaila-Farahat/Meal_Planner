package com.example.mealplanner.models.mealModel;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("strIngredient")
    private String name;
    private String imageUrl;
    private String measurement;

    public Ingredient(String name, String imageUrl,String measurement) {
        this.name = name;
        this.imageUrl = "https://www.themealdb.com/images/ingredients/" + name + "-Small.png";
        this.measurement = measurement;
    }

    public String getName() {

        return name;
    }

    public String getImageUrl() {

        return imageUrl;
    }

    public String getMeasurement() {

        return measurement;
    }

    public void setName(String name) {
        this.name = name;
        this.imageUrl = "https://www.themealdb.com/images/ingredients/" + name + "-Small.png";
    }

    public void setMeasurement(String measurement) {

        this.measurement = measurement;
    }
}
