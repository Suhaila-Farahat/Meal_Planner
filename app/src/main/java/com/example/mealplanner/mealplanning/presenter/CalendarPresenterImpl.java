package com.example.mealplanner.mealplanning.presenter;

import android.annotation.SuppressLint;
import com.example.mealplanner.database.LocalDataSource;
import com.example.mealplanner.mealplanning.view.CalendarView;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.network.RemoteDataSource;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CalendarPresenterImpl implements CalendarPresenter {
    private final CalendarView view;
    private final MealRepository mealRepository;

    public CalendarPresenterImpl(CalendarView view) {
        this.view = view;
        this.mealRepository = MealRepository.getInstance(
                RemoteDataSource.getInstance(),
                LocalDataSource.getInstance(null)
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchPlannedMeals() {
        mealRepository.getAllScheduledMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::showPlannedMeals, throwable -> view.showError("Failed to load meals"));
    }

    @Override
    public Completable deletePlannedMeal(String mealId) {
        return mealRepository.removeScheduledMeal(mealId);
    }
}
