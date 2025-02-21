package com.example.mealplanner.models.mealModel;

import com.example.mealplanner.network.MealApiService;


import io.reactivex.rxjava3.core.Single;

public class MealRepository {

    //MealLocalDataSource localDataSource;
    MealApiService remoteDataSource;

    private static MealRepository repo = null;

    public MealRepository( MealApiService remoteDataSource) {
      //  this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public static MealRepository getInstance(MealApiService remoteDataSource) {
        if (repo == null) {
            repo = new MealRepository(remoteDataSource);
        }
        return repo;
    }

//    public Flowable<List<Meal>> getAllMeals() {
//        return localDataSource.getMeals();
//    }

//    public void insert(Meal meal) {
//        localDataSource.insert(meal);
//    }

//    public void delete(Meal meal) {
//        localDataSource.delete(meal);
//    }

    public Single<NetworkResponse> getRandomMealFromNetwork() {
        return remoteDataSource.getRandomMeal();
    }
}
