package com.example.mealplanner.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface FavoriteMealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFavorite(FavoriteMeal meal);

    @Query("DELETE FROM favorite_meals WHERE id = :mealId")
    Completable deleteFavorite(String mealId);

    @Query("SELECT * FROM favorite_meals ORDER BY name ASC")
    Flowable<List<FavoriteMeal>> getAllFavorites();

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals WHERE id = :mealId)")
    Single<Boolean> isMealFavorite(String mealId);

    @Query("SELECT * FROM favorite_meals WHERE id = :mealId LIMIT 1")
    Maybe<FavoriteMeal> getMealById(String mealId);
}
