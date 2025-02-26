package com.example.mealplanner.util;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthUtils {

    private static final String PREF_NAME = "MealPlannerPrefs";
    private static final String PREF_USER_LOGGED_IN = "user_logged_in";
    private static final String PREF_USER_ID = "user_id";

    public static void saveLoginState(Context context, boolean isLoggedIn, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_USER_LOGGED_IN, isLoggedIn);
        editor.putString(PREF_USER_ID, userId);
        editor.apply();
    }

    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREF_USER_LOGGED_IN, false);
    }

    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_USER_ID, null);
    }
}
