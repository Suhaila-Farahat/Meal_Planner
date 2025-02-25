package com.example.mealplanner.mealplanning.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;


@Database(entities = {PlannedMeal.class}, version = 1)
public abstract class PlannedDatabase extends RoomDatabase {
    private static volatile PlannedDatabase instance;

    public abstract PlannedMealDao plannedMealDao();

    public static synchronized PlannedDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            PlannedDatabase.class, "planned_meals_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
