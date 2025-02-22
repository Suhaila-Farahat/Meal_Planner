package com.example.mealplanner.util;


import com.example.mealplanner.R;
import com.example.mealplanner.models.flagsModel.CountryFlag;

import java.util.ArrayList;
import java.util.List;

public class FlagUtils {
    public static List<CountryFlag> getCountryFlags() {
        List<CountryFlag> countryFlags = new ArrayList<>();
        countryFlags.add(new CountryFlag("Egypt", R.drawable.egypt));
        countryFlags.add(new CountryFlag("USA", R.drawable.america));
        countryFlags.add(new CountryFlag("UK", R.drawable.britain));
        countryFlags.add(new CountryFlag("Turkey", R.drawable.turkey));
        countryFlags.add(new CountryFlag("Japan", R.drawable.japan));
        countryFlags.add(new CountryFlag("Canada", R.drawable.canada));
        countryFlags.add(new CountryFlag("China", R.drawable.china));
        countryFlags.add(new CountryFlag("Croatia", R.drawable.coratia));
        countryFlags.add(new CountryFlag("Germany", R.drawable.dutch));
        countryFlags.add(new CountryFlag("France", R.drawable.france));
        countryFlags.add(new CountryFlag("Greece", R.drawable.greece));
        countryFlags.add(new CountryFlag("India", R.drawable.india));
        countryFlags.add(new CountryFlag("Ireland", R.drawable.ireland));
        countryFlags.add(new CountryFlag("Italy", R.drawable.italy));
        countryFlags.add(new CountryFlag("Jamaica", R.drawable.jamaica));
        countryFlags.add(new CountryFlag("Kenya", R.drawable.kenya));
        countryFlags.add(new CountryFlag("Malaysia", R.drawable.malaysia));
        countryFlags.add(new CountryFlag("mexico", R.drawable.mexico));
        countryFlags.add(new CountryFlag("Morocco", R.drawable.morocco));
        countryFlags.add(new CountryFlag("Philippine", R.drawable.philippine));
        countryFlags.add(new CountryFlag("Poland", R.drawable.poland));
        countryFlags.add(new CountryFlag("Portugal", R.drawable.portugal));
        countryFlags.add(new CountryFlag("Russia", R.drawable.russia));
        countryFlags.add(new CountryFlag("Spain", R.drawable.spain));
        countryFlags.add(new CountryFlag("Thailand", R.drawable.thailand));
        countryFlags.add(new CountryFlag("Tunisia", R.drawable.tunisia));
        countryFlags.add(new CountryFlag("Ukraine", R.drawable.ukraine));
        countryFlags.add(new CountryFlag("Uruguay", R.drawable.uruguay));
        countryFlags.add(new CountryFlag("Vietnam", R.drawable.vietnam));

        return countryFlags;
    }
    public static String getCountryAPIName(String countryName) {
        switch (countryName) {
            case "Canada":
                return "Canadian";
            case "UK":
                return "British";
            case "USA":
                return "American";
            case "Turkey":
                return "Turkish";
            case "Japan":
                return "Japanese";
            case "China":
                return "Chinese";
            case "Germany":
                return "German";
            case "France":
                return "French";
            case "Italy":
                return "Italian";
            case "Greece":
                return "Greek";
            case "India":
                return "Indian";
            case "Ireland":
                return "Irish";
            case "Mexico":
                return "Mexican";
            case "Morocco":
                return "Moroccan";
            case "Philippines":
                return "Filipino";
            case "Poland":
                return "Polish";
            case "Portugal":
                return "Portuguese";
            case "Russia":
                return "Russian";
            case "Spain":
                return "Spanish";
            case "Thailand":
                return "Thai";
            case "Tunisia":
                return "Tunisian";
            case "Ukraine":
                return "Ukrainian";
            case "Uruguay":
                return "Uruguayan";
            case "Vietnam":
                return "Vietnamese";
            case "Croatia":
                return "Croatian";
            case "Netherlands":
                return "Dutch";
            case "Egypt":
                return "Egyptian";
            case "Kenya":
                return "Kenyan";
            case "Malaysia":
                return "Malaysian";
            case "Norway":
                return "Norwegian";
            default:
                return countryName;
        }
    }


}


























