package com.example.mealplanner.network;

import com.example.mealplanner.model.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface MealApiService {
    @GET("random.php")
    Single<MealResponse> getRandomMeal();
}
