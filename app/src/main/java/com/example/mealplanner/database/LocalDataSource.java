package com.example.mealplanner.database;

import android.content.Context;

import com.example.mealplanner.mealplanning.model.PlannedDatabase;
import com.example.mealplanner.mealplanning.model.PlannedMeal;
import com.example.mealplanner.mealplanning.model.PlannedMealDao;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class LocalDataSource {
    private final FavoriteMealDao favoriteMealDao;
    private final PlannedMealDao plannedMealDao;
    private static volatile LocalDataSource instance = null;

    private LocalDataSource(Context context) {
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        PlannedDatabase pdb = PlannedDatabase.getInstance(context.getApplicationContext());
        this.favoriteMealDao = db.favoriteMealDao();
        this.plannedMealDao = pdb.plannedMealDao();
    }

    public static LocalDataSource getInstance(Context context) {
        if (instance == null) {
            synchronized (LocalDataSource.class) {
                if (instance == null) {
                    instance = new LocalDataSource(context);
                }
            }
        }
        return instance;
    }

    // Favorite Meal Operations
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

    // Planned Meal Operations
    public Completable insertPlannedMeal(PlannedMeal meal) {
        return plannedMealDao.insertPlannedMeal(meal);
    }

    public Completable deletePlannedMeal(String mealId) {
        return plannedMealDao.deletePlannedMeal(mealId);
    }

    public Flowable<List<PlannedMeal>> getAllPlannedMeals() {
        return plannedMealDao.getAllPlannedMeals();
    }

    public Maybe<PlannedMeal> getPlannedMealById(String mealId) {
        return plannedMealDao.getPlannedMealById(mealId);
    }
}
