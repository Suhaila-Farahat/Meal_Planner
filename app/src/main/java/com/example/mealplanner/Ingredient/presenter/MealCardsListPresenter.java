package com.example.mealplanner.Ingredient.presenter;


import com.example.mealplanner.Ingredient.view.MealCardsListView;
import com.example.mealplanner.models.responses.MealResponse;
import com.example.mealplanner.network.MealApiService;
import com.example.mealplanner.network.RetrofitClient;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealCardsListPresenter {

    private MealCardsListView view;
    private MealApiService apiService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MealCardsListPresenter(MealCardsListView view) {
        this.view = view;
        this.apiService = RetrofitClient.getInstance().create(MealApiService.class);
    }

    public void fetchMealsByIngredient(String ingredient) {
        compositeDisposable.add(apiService.getMealsByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (MealResponse response) -> view.showMeals(response.getMeals()),
                        throwable -> view.showError("Failed to load meals: " + throwable.getMessage())
                ));
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }
}
