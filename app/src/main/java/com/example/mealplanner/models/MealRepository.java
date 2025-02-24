package com.example.mealplanner.models;

import com.example.mealplanner.database.FavoriteMeal;
import com.example.mealplanner.database.LocalDataSource;
import com.example.mealplanner.models.responses.CategoryResponse;
import com.example.mealplanner.models.responses.IngredientResponse;
import com.example.mealplanner.models.responses.MealDetailsResponse;
import com.example.mealplanner.models.responses.MealResponse;
import com.example.mealplanner.network.RemoteDataSource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class MealRepository {
    private static  MealRepository instance = null;
    private  RemoteDataSource remoteDataSource;
    private  LocalDataSource localDataSource;

    private MealRepository(RemoteDataSource remoteSource, LocalDataSource localSource) {
        this.remoteDataSource = remoteSource;
        this.localDataSource = localSource;
    }

    public static MealRepository getInstance(RemoteDataSource remoteSource, LocalDataSource localSource) {
        if (instance == null) {
            synchronized (MealRepository.class) {
                if (instance == null) {
                    instance = new MealRepository(remoteSource, localSource);
                }
            }
        }
        return instance;
    }

    // Remote API Calls
    public Single<MealResponse> getRandomMeal() {
        return remoteDataSource.getRandomMeal();
    }

    public Single<CategoryResponse> getCategories() {
        return remoteDataSource.getCategories();
    }

    public Single<MealResponse> getFilteredMealsByCategory(String categoryName) {
        return remoteDataSource.getFilteredMealsByCategory(categoryName);
    }

    public Single<MealResponse> getFilteredMealsByCountry(String countryName) {
        return remoteDataSource.getFilteredMealsByCountry(countryName);
    }

    public Single<MealDetailsResponse> getMealDetails(String mealId) {
        return remoteDataSource.getMealDetails(mealId);
    }

    public Single<MealResponse> getMealsByIngredient(String ingredient) {
        return remoteDataSource.getMealsByIngredient(ingredient);
    }

    public Single<IngredientResponse> getIngredients() {
        return remoteDataSource.getIngredients();
    }

    // Local Database Operations
    public Completable addMealToFavorites(FavoriteMeal meal) {
        return localDataSource.insertFavoriteMeal(meal);
    }

    public Completable removeMealFromFavorites(String mealId) {
        return localDataSource.deleteFavoriteMeal(mealId);
    }

    public Flowable<List<FavoriteMeal>> getAllFavoriteMeals() {
        return localDataSource.getAllFavoriteMeals();
    }

    public Single<Boolean> isMealFavorite(String mealId) {
        return localDataSource.isMealFavorite(mealId);
    }

    public Maybe<FavoriteMeal> getFavoriteMealById(String mealId) {
        return localDataSource.getFavoriteMealById(mealId);
    }
}
