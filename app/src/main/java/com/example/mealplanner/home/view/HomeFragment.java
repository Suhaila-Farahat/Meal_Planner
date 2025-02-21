package com.example.mealplanner.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.home.presenter.HomePresenter;
import com.example.mealplanner.home.presenter.HomePresenterImpl;
import com.example.mealplanner.home.view.categories.CategoryAdapter;
import com.example.mealplanner.home.view.countries.AllFlagsFragment;
import com.example.mealplanner.home.view.countries.CountryFlagAdapter;
import com.example.mealplanner.models.flagsModel.CountryFlag;
import com.example.mealplanner.models.mealModel.Meal;
import com.example.mealplanner.models.mealModel.MealCategory;
import java.util.List;

public class HomeFragment extends Fragment implements AllMealsView {
    private TextView mealNameTextView, seeAllText;
    private ImageView mealImageView;
    private HomePresenter homePresenter;
    private RecyclerView categoryRecyclerView, flagRecyclerView;

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mealNameTextView = view.findViewById(R.id.meal_name);
        mealImageView = view.findViewById(R.id.meal_image);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        flagRecyclerView = view.findViewById(R.id.flag_recycler_view);
        seeAllText = view.findViewById(R.id.see_all_text);

        homePresenter = new HomePresenterImpl(this);
        homePresenter.getMeals();
        homePresenter.getCategories();
        homePresenter.getFlags();

        seeAllText.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new AllFlagsFragment())
                        .addToBackStack(null)
                        .commit()
        );

        return view;
    }

    @Override
    public void showMeal(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            Meal meal = meals.get(0);
            mealNameTextView.setText(meal.getName());
            Glide.with(this).load(meal.getImageUrl()).into(mealImageView);
        } else {
            showError("No meals available");
        }
    }

    @Override
    public void showCategories(List<MealCategory> categories) {
        CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showFlags(List<CountryFlag> flags) {
        CountryFlagAdapter adapter = new CountryFlagAdapter(flags, getContext());
        flagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        flagRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
