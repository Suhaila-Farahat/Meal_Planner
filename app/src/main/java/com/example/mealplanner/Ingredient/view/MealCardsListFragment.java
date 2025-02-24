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
import com.example.mealplanner.mealDetails.view.MealDetailsFragment;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.models.mealModel.Meal;
import com.example.mealplanner.network.RemoteDataSource;

import java.util.List;

public class MealCardsListFragment extends Fragment implements MealCardsListView {
    private RecyclerView mealRecyclerView;
    private MealCardsListPresenter presenter;
    private String ingredientName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_list, container, false);

        mealRecyclerView = view.findViewById(R.id.meal_recycler_view);
        mealRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            ingredientName = getArguments().getString("ingredientName");

            MealRepository mealRepository = new MealRepository(RemoteDataSource.getInstance(), LocalDataSource.getInstance(AppDatabase.getInstance(getContext()).favoriteMealDao()));
            presenter = new MealCardsListPresenterImpl(this, mealRepository);
            presenter.fetchMealsByIngredient(ingredientName);
        }

        return view;
    }

    @Override
    public void showMeals(List<Meal> meals) {
        MealAdapter adapter = new MealAdapter(meals, this::navigateToMealDetails);
        mealRecyclerView.setAdapter(adapter);
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
}
