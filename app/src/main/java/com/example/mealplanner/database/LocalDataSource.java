package com.example.mealplanner.database;


import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class LocalDataSource {
    private static volatile LocalDataSource instance;
    private final FavoriteMealDao favoriteMealDao;

    public LocalDataSource(FavoriteMealDao favoriteMealDao) {
        this.favoriteMealDao = favoriteMealDao;
    }

    public static LocalDataSource getInstance(FavoriteMealDao favoriteMealDao) {
        if (instance == null) {
            synchronized (LocalDataSource.class) {
                if (instance == null) {
                    instance = new LocalDataSource(favoriteMealDao);
                }
            }
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
