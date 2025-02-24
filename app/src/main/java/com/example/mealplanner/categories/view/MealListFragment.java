package com.example.mealplanner.categories.view;

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
import com.example.mealplanner.R;
import com.example.mealplanner.categories.presenter.MealListPresenter;
import com.example.mealplanner.categories.presenter.MealListPresenterImp;
import com.example.mealplanner.database.AppDatabase;
import com.example.mealplanner.database.FavoriteMealDao;
import com.example.mealplanner.database.LocalDataSource;
import com.example.mealplanner.mealDetails.view.MealDetailsFragment;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.models.mealModel.Meal;
import com.example.mealplanner.network.RemoteDataSource;
import java.util.List;

public class MealListFragment extends Fragment implements MealListView {
    private RecyclerView recyclerView;
    private MealListPresenter presenter;
    private MealListAdapter adapter;

    public MealListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_list, container, false);
        setupRecyclerView(view);
        setupPresenter();

        if (getArguments() != null) {
            String categoryName = getArguments().getString("category_name");
            if (categoryName != null) {
                presenter.fetchMeals(categoryName);
            }
        }

        return view;
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.meal_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FavoriteMealDao favoriteMealDao = AppDatabase.getInstance(requireContext()).favoriteMealDao();
        adapter = new MealListAdapter(this::navigateToMealDetails, favoriteMealDao, () -> {});
        recyclerView.setAdapter(adapter);
    }

    private void setupPresenter() {
        MealRepository mealRepository = MealRepository.getInstance(
                RemoteDataSource.getInstance(),
                LocalDataSource.getInstance(requireContext())
        );
        presenter = new MealListPresenterImp(this, mealRepository);
    }

    @Override
    public void showMeals(List<Meal> meals) {
        adapter.setMeals(meals);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMealDetails(Meal meal) {
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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
