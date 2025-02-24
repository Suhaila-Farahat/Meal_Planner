package com.example.mealplanner.countries.presenter;

public interface CountryMealListPresenter {
    void fetchMealsByCountry(String countryName);
    void onDestroy();
}
