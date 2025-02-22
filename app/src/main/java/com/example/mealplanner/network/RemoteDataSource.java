package com.example.mealplanner.network;

import com.example.mealplanner.models.NetworkResponse;
import io.reactivex.rxjava3.core.Single;

public class RemoteDataSource {
    private static volatile RemoteDataSource instance;
    private final MealApiService mealApiService;

    private RemoteDataSource() {
        mealApiService = RetrofitClient.getInstance().create(MealApiService.class);
    }

    public static RemoteDataSource getInstance() {
        if (instance == null) {
            synchronized (RemoteDataSource.class) {
                if (instance == null) {
                    instance = new RemoteDataSource();
                }
            }
        }
        return instance;
    }

    public Single<NetworkResponse> getRandomMeal() {
        return mealApiService.getRandomMeal();
    }

    public Single<NetworkResponse> getCategories() {
        return mealApiService.getCategories();
    }

    public Single<NetworkResponse> getFilteredMealsByCategory(String category) {
        return mealApiService.getFilteredMealsByCategory(category);
    }

    public Single<NetworkResponse> getFilteredMealsByCountry(String country) {
        return mealApiService.getFilteredMealsByCountry(country);
    }
}
