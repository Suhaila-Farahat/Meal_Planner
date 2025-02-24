package com.example.mealplanner.categories.presenter;

import com.example.mealplanner.categories.view.MealListView;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.models.mealModel.Meal;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;

public class MealListPresenterImp implements MealListPresenter {
    private final MealListView view;
    private final MealRepository mealRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MealListPresenterImp(MealListView view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }

    @Override
    public void fetchMeals(String categoryName) {
        compositeDisposable.add(
                mealRepository.getFilteredMealsByCategory(categoryName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> handleMealResponse(response.getMeals()),
                                throwable -> view.showError("Failed to load meals: " + throwable.getLocalizedMessage())
                        )
        );
    }

    private void handleMealResponse(List<Meal> meals) {
        if (meals == null || meals.isEmpty()) {
            view.showError("No meals found!");
        } else {
            view.showMeals(meals);
        }
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
    }
}
