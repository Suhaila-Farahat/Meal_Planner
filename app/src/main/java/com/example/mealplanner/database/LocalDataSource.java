package com.example.mealplanner.database;


import android.content.Context;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class LocalDataSource {
    private final FavoriteMealDao favoriteMealDao;
    private static LocalDataSource instance = null;

    private LocalDataSource(Context context) {
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        this.favoriteMealDao = db.favoriteMealDao();
    }

    public static LocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new LocalDataSource(context);
        }
        return instance;
    }


    public Completable insertFavoriteMeal(FavoriteMeal meal) {
        return favoriteMealDao.insertFavorite(meal);
    }

    public Completable deleteFavoriteMeal(String mealId) {
        return favoriteMealDao.deleteFavorite(mealId);
    }

    public Flowable<List<FavoriteMeal>> getAllFavoriteMeals() {
        return favoriteMealDao.getAllFavorites();
    }

    public Single<Boolean> isMealFavorite(String mealId) {
        return favoriteMealDao.isMealFavorite(mealId);
    }

    public Maybe<FavoriteMeal> getFavoriteMealById(String mealId) {
        return favoriteMealDao.getMealById(mealId);
    }
}
