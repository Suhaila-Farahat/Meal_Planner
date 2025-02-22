package com.example.mealplanner.countries.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealplanner.R;
import com.example.mealplanner.countries.presenter.CountryMealListPresenter;
import com.example.mealplanner.models.mealModel.Meal;
import java.util.List;

public class CountryMealListFragment extends Fragment implements CountryMealListView {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private CountryMealListPresenter presenter;
    private CountryMealListAdapter adapter;

    public CountryMealListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_list, container, false);

        recyclerView = view.findViewById(R.id.meal_recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CountryMealListAdapter(getContext());
        recyclerView.setAdapter(adapter);

        presenter = new CountryMealListPresenter(this);

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
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}

