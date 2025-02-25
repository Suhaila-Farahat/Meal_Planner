package com.example.mealplanner;


import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "AppPrefs";
    private static final String KEY_IS_GUEST = "IS_GUEST";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setGuestMode(boolean isGuest) {
        editor.putBoolean(KEY_IS_GUEST, isGuest);
        editor.apply();
    }

    public boolean isGuest() {
        return prefs.getBoolean(KEY_IS_GUEST, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}

