package com.example.mealplanner;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.mealplanner.database.FavoriteMeal;
import com.example.mealplanner.database.LocalDataSource;
import com.example.mealplanner.mealplanning.model.PlannedMeal;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.network.RemoteDataSource;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class BackupManager {

    private static final String TAG = "BackupManager";
    private final FirebaseFirestore firestore;
    private final MealRepository repository;

    public BackupManager(Context context) {
        firestore = FirebaseFirestore.getInstance();
        repository = MealRepository.getInstance(RemoteDataSource.getInstance(), LocalDataSource.getInstance(context));
    }
    @SuppressLint("CheckResult")
    public void backupData(String username) {
        repository.getAllFavoriteMeals()
                .firstOrError()
                .subscribe(favorites -> {
                    List<Map<String, Object>> favoritesMapList = new ArrayList<>();
                    for (FavoriteMeal favorite : favorites) {
                        Map<String, Object> favoriteMap = new HashMap<>();
                        favoriteMap.put("mealId", favorite.getId());
                        favoriteMap.put("mealName", favorite.getName());
                        favoriteMap.put("mealImage", favorite.getImageUrl());
                        favoritesMapList.add(favoriteMap);
                    }

                    Map<String, Object> favoritesMap = new HashMap<>();
                    favoritesMap.put("favorites", favoritesMapList);

                    firestore.collection("users")
                            .document(username)
                            .collection("backup")
                            .document("favorites")
                            .set(favoritesMap)
                            .addOnSuccessListener(aVoid -> Log.i(TAG, "Favorites backup successful"))
                            .addOnFailureListener(e -> Log.e(TAG, "Error backing up favorites", e));
                }, throwable -> Log.e(TAG, "Error fetching favorites for backup", throwable));

        repository.getAllScheduledMeals()
                .firstOrError()
                .subscribe(plans -> {
                    List<Map<String, Object>> plansMapList = new ArrayList<>();
                    for (PlannedMeal meal : plans) {
                        Map<String, Object> planMap = new HashMap<>();
                        planMap.put("mealId", meal.getMealId());
                        planMap.put("mealName", meal.getMealName());
                        planMap.put("mealImage", meal.getMealImage());
                        planMap.put("plannedDate", meal.getPlannedDate());
                        plansMapList.add(planMap);
                    }

                    Map<String, Object> plansMap = new HashMap<>();
                    plansMap.put("meal_plans", plansMapList);

                    firestore.collection("users")
                            .document(username)
                            .collection("backup")
                            .document("meal_plans")
                            .set(plansMap)
                            .addOnSuccessListener(aVoid -> Log.i(TAG, "Meal plans backup successful"))
                            .addOnFailureListener(e -> Log.e(TAG, "Error backing up meal plans", e));
                }, throwable -> Log.e(TAG, "Error fetching meal plans for backup", throwable));
    }

    @SuppressLint("CheckResult")
    public void restoreData(String username) {
        firestore.collection("users")
                .document(username)
                .collection("backup")
                .document("favorites")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> favoritesMapList = (List<Map<String, Object>>) documentSnapshot.get("favorites");
                        if (favoritesMapList != null) {
                            for (Map<String, Object> map : favoritesMapList) {
                                String mealId = (String) map.get("mealId");
                                String mealName = (String) map.get("mealName");
                                String mealImage = (String) map.get("mealImage");

                                FavoriteMeal favorite = new FavoriteMeal(mealId, mealName, mealImage);
                                repository.addMealToFavorites(favorite)
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(() -> Log.i(TAG, "Restored favorite: " + mealId),
                                                throwable -> Log.e(TAG, "Error restoring favorite", throwable));
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error restoring favorites", e));

        firestore.collection("users")
                .document(username)
                .collection("backup")
                .document("meal_plans")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> plansMapList = (List<Map<String, Object>>) documentSnapshot.get("meal_plans");
                        if (plansMapList != null) {
                            for (Map<String, Object> map : plansMapList) {
                                String mealId = (String) map.get("mealId");
                                String mealName = (String) map.get("mealName");
                                String mealImage = (String) map.get("mealImage");
                                long plannedDate = (long) map.get("plannedDate");

                                PlannedMeal meal = new PlannedMeal();
                                meal.setMealId(mealId);
                                meal.setMealName(mealName);
                                meal.setMealImage(mealImage);
                                meal.setPlannedDate(plannedDate);

                                repository.scheduleMeal(meal)
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(() -> Log.i(TAG, "Restored meal plan: " + mealId),
                                                throwable -> Log.e(TAG, "Error restoring meal plan", throwable));
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error restoring meal plans", e));
    }
}
