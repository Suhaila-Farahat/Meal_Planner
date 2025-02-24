package com.example.mealplanner.home.presenter;

import android.annotation.SuppressLint;

import com.example.mealplanner.home.view.AllMealsView;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.models.flagsModel.CountryFlag;
import com.example.mealplanner.models.responses.MealResponse;
import com.example.mealplanner.models.responses.CategoryResponse;
import com.example.mealplanner.util.FlagUtils;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImpl implements HomePresenter {
    private final MealRepository mealRepository;
    private final AllMealsView view;

    public HomePresenterImpl(AllMealsView view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getMeals() {
        mealRepository.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleMealSuccess, this::handleMealError);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getCategories() {
        mealRepository.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleCategorySuccess, this::handleCategoryError);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getFlags() {
        Single.fromCallable(FlagUtils::getCountryFlags)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleFlagsSuccess, this::handleFlagsError);
    }

    private void handleMealSuccess(MealResponse mealResponse) {
        if (mealResponse.getMeals() != null && !mealResponse.getMeals().isEmpty()) {
            view.showMeal(mealResponse.getMeals());
        } else {
            view.showError("No meals found");
        }
    }

    private void handleMealError(Throwable e) {
        view.showError("Error fetching meals: " + e.getMessage());
    }

    private void handleCategorySuccess(CategoryResponse categoryResponse) {
        if (categoryResponse.getCategories() != null && !categoryResponse.getCategories().isEmpty()) {
            view.showCategories(categoryResponse.getCategories());
        } else {
            view.showError("No categories found");
        }
    }

    private void handleCategoryError(Throwable e) {
        view.showError("Error fetching categories: " + e.getMessage());
    }

    private void handleFlagsSuccess(List<CountryFlag> flags) {
        if (flags != null && !flags.isEmpty()) {
            view.showFlags(flags.subList(0, Math.min(6, flags.size())));
        } else {
            view.showError("No flags found");
        }
    }

    private void handleFlagsError(Throwable e) {
        view.showError("Error fetching flags: " + e.getMessage());
    }
}
