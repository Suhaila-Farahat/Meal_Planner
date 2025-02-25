package com.example.mealplanner.mealplanning.presenter;

import io.reactivex.rxjava3.core.Completable;

public interface CalendarPresenter {
    void fetchPlannedMeals();
    Completable deletePlannedMeal(String mealId);
}
