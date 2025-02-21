package com.example.mealplanner.network;

import com.example.mealplanner.models.mealModel.NetworkResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface MealApiService {
    @GET("random.php")
    Single<NetworkResponse> getRandomMeal();
    @GET("categories.php")
    Single<NetworkResponse> getCategories();
}
