package com.example.mealplanner.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealplanner.Ingredient.view.IngredientSelectionFragment;
import com.example.mealplanner.R;
import com.example.mealplanner.categories.view.MealListFragment;
import com.example.mealplanner.countries.view.AllFlagsFragment;
import com.example.mealplanner.countries.view.CountryMealListFragment;
import com.example.mealplanner.home.presenter.HomePresenter;
import com.example.mealplanner.home.presenter.HomePresenterImpl;
import com.example.mealplanner.mealDetails.view.MealDetailsFragment;
import com.example.mealplanner.models.flagsModel.CountryFlag;
import com.example.mealplanner.models.mealModel.Meal;
import com.example.mealplanner.models.mealModel.MealCategory;
import com.example.mealplanner.util.FlagUtils;

import java.util.List;

public class HomeFragment extends Fragment implements AllMealsView {
    private TextView mealNameTextView, seeAllFlags;
    private ImageView mealImageView;
    private RecyclerView categoryRecyclerView, flagRecyclerView;
    private HomePresenter homePresenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mealNameTextView = view.findViewById(R.id.meal_name);
        mealImageView = view.findViewById(R.id.meal_image);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        flagRecyclerView = view.findViewById(R.id.flag_recycler_view);
        seeAllFlags = view.findViewById(R.id.see_all_text);
        Button selectIngredientsText = view.findViewById(R.id.selectIngredientsButton);


        homePresenter = new HomePresenterImpl(this);

        homePresenter.getMeals();
        homePresenter.getCategories();
        homePresenter.getFlags();

        seeAllFlags.setOnClickListener(v -> {
            List<CountryFlag> flags = FlagUtils.getCountryFlags();

            AllFlagsFragment allFlagsFragment = AllFlagsFragment.newInstance(flags);
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, allFlagsFragment)
                    .addToBackStack(null)
                    .commit();
        });
        selectIngredientsText.setOnClickListener(v -> {
            Fragment ingredientSelectionFragment = new IngredientSelectionFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ingredientSelectionFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    @Override
    public void showMeal(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            Meal meal = meals.get(0);
            mealNameTextView.setText(meal.getName());
            Glide.with(this).load(meal.getImageUrl()).into(mealImageView);

            mealImageView.setOnClickListener(v -> openMealDetails(meal));
            mealNameTextView.setOnClickListener(v -> openMealDetails(meal));
        } else {
            showError("No meals available");
        }
    }

    private void openMealDetails(Meal meal) {
        MealDetailsFragment mealDetailsFragment = new MealDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mealId", meal.getId());
        mealDetailsFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mealDetailsFragment)
                .addToBackStack(null)
                .commit();
    }



    @Override
    public void showCategories(List<MealCategory> categories) {
        CategoryAdapter adapter = new CategoryAdapter(getContext(), categories, categoryName -> {
            MealListFragment mealListFragment = new MealListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("category_name", categoryName);
            mealListFragment.setArguments(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mealListFragment)
                    .addToBackStack(null)
                    .commit();
        });

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showFlags(List<CountryFlag> flags) {
        CountryFlagAdapter flagAdapter = new CountryFlagAdapter(getContext(), flags, countryName -> {
            String apiCountryName = FlagUtils.getCountryAPIName(countryName);

            CountryMealListFragment fragment = new CountryMealListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("country_name", apiCountryName);
            fragment.setArguments(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        flagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        flagRecyclerView.setAdapter(flagAdapter);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
