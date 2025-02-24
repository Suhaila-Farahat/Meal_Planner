package com.example.mealplanner.countries.presenter;

import android.annotation.SuppressLint;
import com.example.mealplanner.countries.view.CountryMealListView;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.models.mealModel.Meal;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;

public class CountryMealListPresenterImp implements CountryMealListPresenter {
    private CountryMealListView view;
    private final MealRepository mealRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CountryMealListPresenterImp(CountryMealListView view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchMealsByCountry(String countryName) {
        if (view == null) return;

        compositeDisposable.add(
                mealRepository.getFilteredMealsByCountry(countryName)
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
                                throwable -> view.showError("Failed to load meals: " + throwable.getMessage())
                        )
        );
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        view = null;
    }
}
