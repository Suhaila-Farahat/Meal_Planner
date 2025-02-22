package com.example.mealplanner.mealDetails.presenter;

import com.example.mealplanner.mealDetails.view.MealDetailsView;
import com.example.mealplanner.models.mealModel.MealDetails;
import com.example.mealplanner.models.responses.MealDetailsResponse;
import com.example.mealplanner.network.MealApiService;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter {

    private MealDetailsView mealDetailsView;
    private MealApiService mealApiService;

    public MealDetailsPresenter(MealDetailsView mealDetailsView, MealApiService mealApiService) {
        this.mealDetailsView = mealDetailsView;
        this.mealApiService = mealApiService;
    }

    public void fetchMealDetails(String mealId) {
        mealDetailsView.showLoading();

        mealApiService.getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MealDetailsResponse>() {

                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull MealDetailsResponse response) {
                        mealDetailsView.hideLoading();
                        if (response.getMealDetails() != null && !response.getMealDetails().isEmpty()) {
                            MealDetails mealDetails = response.getMealDetails().get(0);
                            mealDetailsView.showMealDetails(mealDetails);
                        } else {
                            mealDetailsView.showError("No meal details found");
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        mealDetailsView.hideLoading();
                        mealDetailsView.showError("Error loading meal details: " + e.getMessage());
                    }
                });
    }
}
