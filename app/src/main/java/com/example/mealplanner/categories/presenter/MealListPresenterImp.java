package com.example.mealplanner.categories.presenter;

import android.annotation.SuppressLint;
import com.example.mealplanner.categories.view.MealListView;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.models.mealModel.Meal;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;

public class MealListPresenterImp implements MealListPresenter {
    private MealListView view;
    private final MealRepository mealRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MealListPresenterImp(MealListView view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchMeals(String categoryName) {
        if (view == null) return;

        compositeDisposable.add(
                mealRepository.getFilteredMealsByCategory(categoryName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    List<Meal> meals = response.getMeals();
                                    if (meals != null && !meals.isEmpty()) {
                                        view.showMeals(meals);
                                    } else {
                                        view.showError("No meals found!");
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
