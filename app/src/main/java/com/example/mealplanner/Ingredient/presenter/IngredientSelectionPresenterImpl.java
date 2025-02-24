package com.example.mealplanner.Ingredient.presenter;

import android.annotation.SuppressLint;
import com.example.mealplanner.Ingredient.view.IngredientSelectionView;
import com.example.mealplanner.models.MealRepository;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class IngredientSelectionPresenterImpl implements IngredientSelectionPresenter {

    private final IngredientSelectionView view;
    private final MealRepository mealRepository;

    public IngredientSelectionPresenterImpl(IngredientSelectionView view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchIngredients() {
        Single.fromCallable(() -> mealRepository.getIngredients().blockingGet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ingredientsResponse -> view.showIngredients(ingredientsResponse.getIngredients()),
                        throwable -> view.showError("Failed to load ingredients: " + throwable.getMessage())
                );
    }
}
