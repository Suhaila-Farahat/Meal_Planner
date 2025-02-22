package com.example.mealplanner.models;



import io.reactivex.rxjava3.core.Single;

import com.example.mealplanner.models.responses.CategoryResponse;
import com.example.mealplanner.models.responses.MealResponse;
import com.example.mealplanner.network.RemoteDataSource;


public class MealRepository {
    private static MealRepository instance;
    private final RemoteDataSource remoteDataSource;

    private MealRepository(RemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static MealRepository getInstance(RemoteDataSource remoteDataSource) {
        if (instance == null) {
            synchronized (MealRepository.class) {
                if (instance == null) {
                    instance = new MealRepository(remoteDataSource);
                }
            }
        }
        return instance;
    }

    public Single<MealResponse> getRandomMeal() {

        return remoteDataSource.getRandomMeal();
    }

    public Single<CategoryResponse> getCategories() {

        return remoteDataSource.getCategories();
    }
}

