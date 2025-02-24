package com.example.mealplanner.countries.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealplanner.R;
import com.example.mealplanner.countries.presenter.CountryMealListPresenterImpl;
import com.example.mealplanner.database.AppDatabase;
import com.example.mealplanner.database.FavoriteMealDao;
import com.example.mealplanner.mealDetails.view.MealDetailsFragment;
import com.example.mealplanner.models.mealModel.Meal;
import java.util.List;
public class CountryMealListFragment extends Fragment implements CountryMealListView, CountryMealListAdapter.OnMealClickListener {
    private RecyclerView recyclerView;
    private CountryMealListPresenterImpl presenter;
    private CountryMealListAdapter adapter;
    private FavoriteMealDao favoriteMealDao;

    public CountryMealListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_list, container, false);

        recyclerView = view.findViewById(R.id.meal_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteMealDao = AppDatabase.getInstance(getContext()).favoriteMealDao();
        adapter = new CountryMealListAdapter(getContext(), this, favoriteMealDao);
        recyclerView.setAdapter(adapter);

        presenter = new CountryMealListPresenterImpl(this);

        if (getArguments() != null) {
            String countryName = getArguments().getString("country_name");
            presenter.fetchMealsByCountry(countryName);
        }

        return view;
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
    public void onMealClick(String mealId) {
        MealDetailsFragment mealDetailsFragment = new MealDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mealId", mealId);
        mealDetailsFragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mealDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
