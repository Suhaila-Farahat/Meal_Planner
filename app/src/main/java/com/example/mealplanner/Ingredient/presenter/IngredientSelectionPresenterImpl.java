package com.example.mealplanner.Ingredient.presenter;


import com.example.mealplanner.Ingredient.view.IngredientSelectionView;
import com.example.mealplanner.network.MealApiService;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class IngredientSelectionPresenterImpl implements IngredientSelectionPresenter {

    private IngredientSelectionView view;
    private MealApiService apiService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public IngredientSelectionPresenterImpl(IngredientSelectionView view, MealApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void fetchIngredients() {
        compositeDisposable.add(apiService.getIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showIngredients(response.getIngredients()),
                        throwable -> view.showError("Failed to load ingredients: " + throwable.getMessage())
                ));
    }
}

