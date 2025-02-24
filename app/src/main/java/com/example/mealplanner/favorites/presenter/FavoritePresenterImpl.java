package com.example.mealplanner.favorites.presenter;

import android.annotation.SuppressLint;

import com.example.mealplanner.database.FavoriteMeal;
import com.example.mealplanner.database.FavoriteMealDao;
import com.example.mealplanner.favorites.view.FavoritesView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenterImpl implements FavoritePresenter {
    private FavoritesView view;
    private FavoriteMealDao favoriteMealDao;
    private CompositeDisposable disposable = new CompositeDisposable();

    public FavoritePresenterImpl(FavoritesView view, FavoriteMealDao favoriteMealDao) {
        this.view = view;
        this.favoriteMealDao = favoriteMealDao;
    }

    @Override
    public void loadFavorites() {
        disposable.add(favoriteMealDao.getAllFavorites()
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
        disposable.add(favoriteMealDao.isMealFavorite(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    if (count) {
                        favoriteMealDao.deleteFavorite(meal.getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> view.showFavoriteRemoved(meal),
                                        throwable -> view.showError(throwable.getMessage()));
                    } else {
                        favoriteMealDao.insertFavorite(meal)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> view.showFavoriteAdded(meal),
                                        throwable -> view.showError(throwable.getMessage()));
                    }
                }, throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void checkIfFavorite(String mealId) {
        disposable.add(favoriteMealDao.isMealFavorite(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> view.onFavoriteChecked(count),
                        throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }
}
