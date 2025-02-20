package com.example.mealplanner.home.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.home.presenter.AllMealsImp;
import com.example.mealplanner.model.Meal;
import com.example.mealplanner.model.MealRepository;
import com.example.mealplanner.network.MealApiService;
import com.example.mealplanner.network.RetrofitClient;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AllMealsView {
    private TextView mealNameTextView;
    private ImageView mealImageView;
    private AllMealsImp allMealsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mealNameTextView = findViewById(R.id.meal_name);
        mealImageView = findViewById(R.id.meal_image);

        MealApiService apiService = RetrofitClient.getInstance().create(MealApiService.class);
        MealRepository mealRepository = new MealRepository(apiService);
        allMealsPresenter = new AllMealsImp(mealRepository, this);

        allMealsPresenter.getMeals();
    }

    @Override
    public void showMeal(List<Meal> mealsList) {
        if (mealsList != null && !mealsList.isEmpty()) {
            Meal meal = mealsList.get(0);
            mealNameTextView.setText(meal.getName());
            Glide.with(this).load(meal.getImageUrl()).into(mealImageView);
        } else {
            showError("No meals available");
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, "Can not display Random Meal ", Toast.LENGTH_SHORT).show();
    }
}
