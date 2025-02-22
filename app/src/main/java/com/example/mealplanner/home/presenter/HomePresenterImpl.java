package com.example.mealplanner.home.presenter;

import com.example.mealplanner.home.view.AllMealsView;
import com.example.mealplanner.models.flagsModel.CountryFlag;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.models.responses.MealResponse;
import com.example.mealplanner.models.responses.CategoryResponse;
import com.example.mealplanner.network.RemoteDataSource;
import com.example.mealplanner.util.FlagUtils;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImpl implements HomePresenter {
    private final MealRepository mealRepository;
    private final AllMealsView IView;

    public HomePresenterImpl(AllMealsView allMealsView) {
        this.mealRepository = MealRepository.getInstance(RemoteDataSource.getInstance());
        this.IView = allMealsView;
    }

    @Override
    public void getMeals() {
        mealRepository.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MealResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

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

    @Override
    public void getCategories() {
        mealRepository.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CategoryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(CategoryResponse categoryResponse) {
                        if (categoryResponse.getCategories() != null && !categoryResponse.getCategories().isEmpty()) {
                            IView.showCategories(categoryResponse.getCategories());
                        } else {
                            IView.showError("No categories found");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        IView.showError("Error fetching categories: " + e.getMessage());
                    }
                });
    }

    @Override
    public void getFlags() {
        Single.fromCallable(FlagUtils::getCountryFlags)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<CountryFlag>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(List<CountryFlag> flags) {
                        if (flags != null && !flags.isEmpty()) {
                            List<CountryFlag> limitedFlags = flags.subList(0, Math.min(6, flags.size()));
                            IView.showFlags(limitedFlags);
                        } else {
                            IView.showError("No flags found");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        IView.showError("Error fetching flags: " + e.getMessage());
                    }
                });
    }
}
