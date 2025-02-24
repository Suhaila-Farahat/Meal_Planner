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
import com.example.mealplanner.database.AppDatabase;
import com.example.mealplanner.database.LocalDataSource;
import com.example.mealplanner.home.presenter.HomePresenter;
import com.example.mealplanner.home.presenter.HomePresenterImpl;
import com.example.mealplanner.mealDetails.view.MealDetailsFragment;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.models.flagsModel.CountryFlag;
import com.example.mealplanner.models.mealModel.Meal;
import com.example.mealplanner.models.mealModel.MealCategory;
import com.example.mealplanner.network.RemoteDataSource;
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
        initViews(view);
        setupPresenter();
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        mealNameTextView = view.findViewById(R.id.meal_name);
        mealImageView = view.findViewById(R.id.meal_image);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        flagRecyclerView = view.findViewById(R.id.flag_recycler_view);
        seeAllFlags = view.findViewById(R.id.see_all_text);
        Button selectIngredientsButton = view.findViewById(R.id.selectIngredientsButton);

        selectIngredientsButton.setOnClickListener(v -> openFragment(new IngredientSelectionFragment()));
    }

    private void setupPresenter() {
        MealRepository mealRepository = MealRepository.getInstance(
                RemoteDataSource.getInstance(),
                LocalDataSource.getInstance(getContext())
        );
        homePresenter = new HomePresenterImpl(this, mealRepository);

        homePresenter.getMeals();
        homePresenter.getCategories();
        homePresenter.getFlags();
    }

    private void setupClickListeners() {
        seeAllFlags.setOnClickListener(v -> openFragment(AllFlagsFragment.newInstance(FlagUtils.getCountryFlags())));
    }

    @Override
    public void showMeal(List<Meal> meals) {
        if (meals == null || meals.isEmpty()) {
            showError("No meals available");
            return;
        }
        Meal meal = meals.get(0);
        mealNameTextView.setText(meal.getName());
        Glide.with(this).load(meal.getImageUrl()).into(mealImageView);

        View.OnClickListener mealClickListener = v -> openMealDetails(meal);
        mealImageView.setOnClickListener(mealClickListener);
        mealNameTextView.setOnClickListener(mealClickListener);
    }

    private void openMealDetails(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putString("mealId", meal.getId());
        MealDetailsFragment mealDetailsFragment = new MealDetailsFragment();
        mealDetailsFragment.setArguments(bundle);
        openFragment(mealDetailsFragment);
    }

    @Override
    public void showCategories(List<MealCategory> categories) {
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(new CategoryAdapter(getContext(), categories, this::openMealListFragment));
    }

    private void openMealListFragment(String categoryName) {
        Bundle bundle = new Bundle();
        bundle.putString("category_name", categoryName);
        MealListFragment mealListFragment = new MealListFragment();
        mealListFragment.setArguments(bundle);
        openFragment(mealListFragment);
    }

    @Override
    public void showFlags(List<CountryFlag> flags) {
        flagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        flagRecyclerView.setAdapter(new CountryFlagAdapter(getContext(), flags, this::openCountryMealListFragment));
    }

    private void openCountryMealListFragment(String countryName) {
        Bundle bundle = new Bundle();
        bundle.putString("country_name", FlagUtils.getCountryAPIName(countryName));
        CountryMealListFragment fragment = new CountryMealListFragment();
        fragment.setArguments(bundle);
        openFragment(fragment);
    }

    private void openFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
