package com.example.mealplanner.countries.presenter;


import android.annotation.SuppressLint;

import com.example.mealplanner.countries.view.CountryMealListView;
import com.example.mealplanner.models.mealModel.Meal;
import com.example.mealplanner.network.RemoteDataSource;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;

public class CountryMealListPresenterImpl implements CountryMealListPresenter{
    private CountryMealListView view;
    private final RemoteDataSource remoteDataSource;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CountryMealListPresenterImpl(CountryMealListView view) {
        this.view = view;
        this.remoteDataSource = RemoteDataSource.getInstance();
    }
    @Override
    @SuppressLint("CheckResult")
    public void fetchMealsByCountry(String countryName) {
        compositeDisposable.add(
                remoteDataSource.getFilteredMealsByCountry(countryName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    List<Meal> meals = response.getMeals();
                                    if (meals != null && !meals.isEmpty()) {
                                        view.showMeals(meals);
                                    } else {
                                        view.showError("No meals found for this country!");
                                    }
                                },
                                throwable -> {
                                    view.showError("Failed to load meals: " + throwable.getMessage());
                                }
                        )
        );
    }
    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        view = null;
    }
}

