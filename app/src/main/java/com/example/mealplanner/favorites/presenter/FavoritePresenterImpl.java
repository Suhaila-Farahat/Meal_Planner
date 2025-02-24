package com.example.mealplanner.favorites.presenter;

import android.annotation.SuppressLint;

import com.example.mealplanner.database.FavoriteMeal;
import com.example.mealplanner.favorites.view.FavoritesView;
import com.example.mealplanner.models.MealRepository;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenterImpl implements FavoritePresenter {
    private final FavoritesView view;
    private final MealRepository repository;

    public FavoritePresenterImpl(FavoritesView view, MealRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadFavorites() {
        repository.getAllFavoriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::showFavorites,
                        throwable -> view.showError("Error loading favorites: " + throwable.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void toggleFavorite(FavoriteMeal meal) {
        repository.isMealFavorite(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isFavorite -> {
                            if (isFavorite) {
                                removeMealFromFavorites(meal);
                            } else {
                                addMealToFavorites(meal);
                            }
                        },
                        throwable -> view.showError("Error toggling favorite: " + throwable.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    private void addMealToFavorites(FavoriteMeal meal) {
        repository.addMealToFavorites(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.showFavoriteAdded(meal),
                        throwable -> view.showError("Error adding to favorites: " + throwable.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    private void removeMealFromFavorites(FavoriteMeal meal) {
        repository.removeMealFromFavorites(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.showFavoriteRemoved(meal),
                        throwable -> view.showError("Error removing from favorites: " + throwable.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void checkIfFavorite(String mealId) {
        repository.isMealFavorite(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::onFavoriteChecked,
                        throwable -> view.showError("Error checking favorite: " + throwable.getMessage())
                );
    }


}
