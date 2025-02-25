package com.example.mealplanner.mealDetails.presenter;

import com.example.mealplanner.mealDetails.view.MealDetailsView;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.models.responses.MealDetailsResponse;
import com.example.mealplanner.mealplanning.model.PlannedMeal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenterImpl implements MealDetailsPresenter {

    private final MealDetailsView view;
    private final MealRepository mealRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public MealDetailsPresenterImpl(MealDetailsView view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }

    @Override
    public void fetchMealDetails(String mealId) {
        disposables.add(
                mealRepository.getMealDetails(mealId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::handleResponse,
                                this::handleError
                        )
        );
    }

    @Override
    public void scheduleMeal(PlannedMeal plannedMeal) {
        disposables.add(
                mealRepository.scheduleMeal(plannedMeal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> view.showMessage("Meal scheduled successfully"),
                                throwable -> view.showError("Error scheduling meal: " + throwable.getMessage())
                        )
        );
    }

    private void handleResponse(MealDetailsResponse response) {
        if (response.getMealDetails() != null && !response.getMealDetails().isEmpty()) {
            view.showMealDetails(response.getMealDetails().get(0));
        } else {
            view.showError("No meal details found");
        }
    }

    private void handleError(Throwable throwable) {
        view.showError("Error loading meal details: " + throwable.getMessage());
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }
}
