package com.example.mealplanner.network;

import com.example.mealplanner.models.responses.CategoryResponse;
import com.example.mealplanner.models.responses.IngredientResponse;
import com.example.mealplanner.models.responses.MealDetailsResponse;
import com.example.mealplanner.models.responses.MealResponse;
import io.reactivex.rxjava3.core.Single;

public class RemoteDataSource {
    private static volatile RemoteDataSource instance;
    private final MealApiService mealApiService;

    private RemoteDataSource(MealApiService mealApiService) {
        this.mealApiService = mealApiService;
    }

    public static RemoteDataSource getInstance() {
        if (instance == null) {
            synchronized (RemoteDataSource.class) {
                if (instance == null) {
                    instance = new RemoteDataSource(RetrofitClient.getInstance().create(MealApiService.class));
                }
            }
        }
        return instance;
    }

    public Single<MealResponse> getRandomMeal() {
        return mealApiService.getRandomMeal();
    }

    public Single<CategoryResponse> getCategories() {
        return mealApiService.getCategories();
    }

    public Single<MealResponse> getFilteredMealsByCategory(String category) {
        return mealApiService.getFilteredMealsByCategory(category);
    }

    public Single<MealResponse> getFilteredMealsByCountry(String country) {
        return mealApiService.getFilteredMealsByCountry(country);
    }

    public Single<MealResponse> getMealsByIngredient(String ingredient) {
        return mealApiService.getMealsByIngredient(ingredient);
    }

    public Single<IngredientResponse> getIngredients() {
        return mealApiService.getIngredients();
    }

    public Single<MealDetailsResponse> getMealDetails(String mealId) {
        return mealApiService.getMealDetails(mealId);
    }
}
