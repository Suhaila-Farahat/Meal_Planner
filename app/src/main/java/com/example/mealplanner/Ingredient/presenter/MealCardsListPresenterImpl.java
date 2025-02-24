package com.example.mealplanner.Ingredient.presenter;

import android.annotation.SuppressLint;
import com.example.mealplanner.Ingredient.view.MealCardsListView;
import com.example.mealplanner.models.MealRepository;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealCardsListPresenterImpl implements MealCardsListPresenter {

    private final MealCardsListView view;
    private final MealRepository mealRepository;

    public MealCardsListPresenterImpl(MealCardsListView view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchMealsByIngredient(String ingredient) {
        mealRepository.getMealsByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showMeals(response.getMeals()),
                        throwable -> view.showError("Failed to load meals: " + throwable.getMessage())
                );
    }
}
