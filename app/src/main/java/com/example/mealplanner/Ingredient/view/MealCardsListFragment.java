package com.example.mealplanner.Ingredient.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealplanner.Ingredient.presenter.MealCardsListPresenter;
import com.example.mealplanner.Ingredient.presenter.MealCardsListPresenterImpl;
import com.example.mealplanner.R;
import com.example.mealplanner.database.AppDatabase;
import com.example.mealplanner.database.LocalDataSource;
import com.example.mealplanner.database.FavoriteMealDao;
import com.example.mealplanner.mealDetails.view.MealDetailsFragment;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.models.mealModel.Meal;
import com.example.mealplanner.network.RemoteDataSource;
import java.util.List;

public class MealCardsListFragment extends Fragment implements MealCardsListView {

    private RecyclerView mealRecyclerView;
    private MealCardsListPresenter presenter;
    private String ingredientName;
    private FavoriteMealDao favoriteMealDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_list, container, false);
        initializeUI(view);
        fetchMeals();
        return view;
    }

    private void initializeUI(View view) {
        mealRecyclerView = view.findViewById(R.id.meal_recycler_view);
        mealRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        favoriteMealDao = AppDatabase.getInstance(getContext()).favoriteMealDao();
    }

    private void fetchMeals() {
        if (getArguments() != null) {
            ingredientName = getArguments().getString("ingredientName");
            if (ingredientName != null) {
                MealRepository mealRepository = MealRepository.getInstance(
                        RemoteDataSource.getInstance(),
                        LocalDataSource.getInstance(getContext())
                );
                presenter = new MealCardsListPresenterImpl(this, mealRepository);
                presenter.fetchMealsByIngredient(ingredientName);
            } else {
                showError("Ingredient name is missing!");
            }
        } else {
            showError("Arguments are missing!");
        }
    }

    @Override
    public void showMeals(List<Meal> meals) {
        if (meals == null || meals.isEmpty()) {
            showError("No meals found.");
            return;
        }

        mealRecyclerView.setAdapter(new MealAdapter(getContext(), meals, this::navigateToMealDetails, favoriteMealDao));
    }

    private void navigateToMealDetails(Meal meal) {
        MealDetailsFragment mealDetailsFragment = new MealDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mealId", meal.getId());
        mealDetailsFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mealDetailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
