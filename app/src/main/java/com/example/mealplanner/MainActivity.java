package com.example.mealplanner;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mealplanner.auth.login.view.LoginActivity;
import com.example.mealplanner.favorites.view.FavoritesFragment;
import com.example.mealplanner.home.view.HomeFragment;
import com.example.mealplanner.mealplanning.view.CalendarPlannedFragment;
import com.example.mealplanner.search.view.SearchFragment;
import com.example.mealplanner.auth.signup.view.SignUpActivity;
import com.example.mealplanner.start.view.StartActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        boolean isGuest = sessionManager.isGuest();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_search) {
                loadFragment(new SearchFragment());
                return true;
            }

            if (isGuest && (itemId == R.id.nav_favorites || itemId == R.id.nav_calender || itemId == R.id.nav_profile)) {
                showSignUpAlert();
                return false;
            }

            Fragment selectedFragment = null;
            if (itemId == R.id.nav_favorites) {
                selectedFragment = new FavoritesFragment();
            } else if (itemId == R.id.nav_calender) {
                selectedFragment = new CalendarPlannedFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }

            return false;
        });

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void showSignUpAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Limited Access")
                .setMessage("Sign up to unlock Favorites, Calendar, and Profile features!")
                .setPositiveButton("Sign Up", (dialog, which) -> {
                    startActivity(new Intent(MainActivity.this, StartActivity.class));
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
