package com.example.mealplanner.categories.presenter;

import android.annotation.SuppressLint;
import com.example.mealplanner.categories.view.MealListView;
import com.example.mealplanner.models.mealModel.Meal;
import com.example.mealplanner.network.RemoteDataSource;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;

public class MealListPresenter {
    private MealListView view;
    private final RemoteDataSource remoteDataSource;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MealListPresenter(MealListView view) {
        this.view = view;
        this.remoteDataSource = RemoteDataSource.getInstance();
    }

    @SuppressLint("CheckResult")
    public void fetchMeals(String categoryName) {
        view.showLoading();
        compositeDisposable.add(
                remoteDataSource.getFilteredMealsByCategory(categoryName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    view.hideLoading();
                                    List<Meal> meals = response.getMeals();
                                    if (meals != null && !meals.isEmpty()) {
                                        view.showMeals(meals);
                                    } else {
                                        view.showError("No meals found!");
                                    }
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.showError("Failed to load meals: " + throwable.getMessage());
                                }
                        )
        );
    }

    public void onDestroy() {
        compositeDisposable.clear();
        view = null;
    }
}
