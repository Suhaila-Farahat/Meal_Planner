package com.example.mealplanner.network;

import com.example.mealplanner.models.responses.IngredientResponse;
import com.example.mealplanner.models.responses.MealResponse;
import com.example.mealplanner.models.responses.CategoryResponse;
import com.example.mealplanner.models.responses.MealDetailsResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiService {

    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("categories.php")
    Single<CategoryResponse> getCategories();

    @GET("filter.php")
    Single<MealResponse> getFilteredMealsByCategory(@Query("c") String category);

    @GET("filter.php")
    Single<MealResponse> getFilteredMealsByCountry(@Query("a") String country);

    @GET("lookup.php")
    Single<MealDetailsResponse> getMealDetails(@Query("i") String mealId);
    @GET("filter.php")
    Single<MealResponse> getMealsByIngredient(@Query("i") String ingredient);
    @GET("list.php?i=list")
    Single<IngredientResponse> getIngredients();

}
