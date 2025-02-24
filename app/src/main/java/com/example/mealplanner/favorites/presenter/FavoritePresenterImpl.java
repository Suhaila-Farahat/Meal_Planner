package com.example.mealplanner.favorites.presenter;

import android.annotation.SuppressLint;

import com.example.mealplanner.database.FavoriteMeal;
import com.example.mealplanner.favorites.view.FavoritesView;
import com.example.mealplanner.models.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenterImpl implements FavoritePresenter {
    private FavoritesView view;
    private MealRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();

    public FavoritePresenterImpl(FavoritesView view, MealRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadFavorites() {
        disposable.add(repository.getAllFavoriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> view.showFavorites(meals),
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    @SuppressLint("CheckResult")
    @Override
    public void toggleFavorite(FavoriteMeal meal) {
        disposable.add(repository.isMealFavorite(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isFavorite -> {
                    if (isFavorite) {
                        disposable.add(repository.removeMealFromFavorites(meal.getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> view.showFavoriteRemoved(meal),
                                        throwable -> view.showError(throwable.getMessage())));
                    } else {
                        disposable.add(repository.addMealToFavorites(meal)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> view.showFavoriteAdded(meal),
                                        throwable -> view.showError(throwable.getMessage())));
                    }
                }, throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void checkIfFavorite(String mealId) {
        disposable.add(repository.isMealFavorite(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isFavorite -> view.onFavoriteChecked(isFavorite),
                        throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }
}
