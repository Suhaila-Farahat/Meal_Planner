package com.example.mealplanner.home.presenter;

import com.example.mealplanner.home.view.AllMealsView;
import com.example.mealplanner.model.MealRepository;
import com.example.mealplanner.model.MealResponse;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AllMealsImp implements AllMeals {

    private MealRepository mealRepository;
    private AllMealsView IView;

    public AllMealsImp(MealRepository mealRepository, AllMealsView allMealsView) {
        this.mealRepository = mealRepository;
        this.IView = allMealsView;
    }

    @Override
    public void getMeals() {
        mealRepository.getRandomMealFromNetwork()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MealResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(MealResponse mealResponse) {
                        if (mealResponse.getMeals() != null && !mealResponse.getMeals().isEmpty()) {
                            IView.showMeal(mealResponse.getMeals());
                        } else {
                            IView.showError("No meals found");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        IView.showError("Error fetching meals: " + e.getMessage());
                    }
                });
    }


//    @Override
//    public void addToFav(Meal meal) {
//        mealRepository.insert(meal);
//    }
}