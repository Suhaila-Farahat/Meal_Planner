package com.example.mealplanner.models.flagsModel;

public class CountryFlag {
    private String countryName;
    private int flagResId;

    public CountryFlag(String countryName, int flagResId) {
        this.countryName = countryName;
        this.flagResId = flagResId;
    }

    public String getCountryName() {
        return countryName;
    }

    public int getFlagResId() {
        return flagResId;
    }
}
