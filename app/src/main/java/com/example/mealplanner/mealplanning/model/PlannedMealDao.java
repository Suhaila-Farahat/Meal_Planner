package com.example.mealplanner.mealplanning.model;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface PlannedMealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertPlannedMeal(PlannedMeal plannedMeal);

    @Query("SELECT * FROM planned_meals ORDER BY plannedDate ASC")
    Flowable<List<PlannedMeal>> getAllPlannedMeals();

    @Delete
    Completable deletePlannedMeal(PlannedMeal meal);

    @Query("DELETE FROM planned_meals WHERE mealId = :mealId")
    Completable deletePlannedMeal(String mealId);

    @Query("SELECT * FROM planned_meals WHERE mealId = :mealId LIMIT 1")
    Maybe<PlannedMeal> getPlannedMealById(String mealId);

}