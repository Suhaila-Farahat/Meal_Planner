package com.example.mealplanner.network;

import com.example.mealplanner.models.NetworkResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiService {
    @GET("random.php")
    Single<NetworkResponse> getRandomMeal();

    @GET("categories.php")
    Single<NetworkResponse> getCategories();

    @GET("filter.php")
    Single<NetworkResponse> getFilteredMealsByCategory(@Query("c") String category);

    @GET("filter.php")
    Single<NetworkResponse> getFilteredMealsByCountry(@Query("a") String country);
}
