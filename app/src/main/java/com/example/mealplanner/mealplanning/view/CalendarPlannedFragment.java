package com.example.mealplanner.mealplanning.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealplanner.R;
import com.example.mealplanner.mealplanning.model.PlannedMeal;
import com.example.mealplanner.mealplanning.presenter.CalendarPresenter;
import com.example.mealplanner.mealplanning.presenter.CalendarPresenterImpl;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CalendarPlannedFragment extends Fragment implements CalendarView, PlannedMealsAdapter.OnMealDeleteListener {

    private RecyclerView plannedMealsRecyclerView;
    private PlannedMealsAdapter adapter;
    private CalendarPresenter calendarPresenter;
    private final List<PlannedMeal> plannedMealsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        initViews(view);
        setupPresenter();
        loadPlannedMeals();
        return view;
    }

    private void initViews(View view) {
        plannedMealsRecyclerView = view.findViewById(R.id.plannedMealsRecyclerView);
        plannedMealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PlannedMealsAdapter(getContext(), plannedMealsList, this);
        plannedMealsRecyclerView.setAdapter(adapter);
    }

    private void setupPresenter() {
        calendarPresenter = new CalendarPresenterImpl(this);
    }

    private void loadPlannedMeals() {
        calendarPresenter.fetchPlannedMeals();
    }

    @Override
    public void showPlannedMeals(List<PlannedMeal> plannedMeals) {
        plannedMealsList.clear();
        plannedMealsList.addAll(plannedMeals);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {}

    @SuppressLint("CheckResult")
    @Override
    public void onMealDeleted(PlannedMeal meal, int position) {
        calendarPresenter.deletePlannedMeal(meal.getMealId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    plannedMealsList.remove(position);
                    adapter.notifyItemRemoved(position);
                }, throwable -> showError("Failed to delete meal"));
    }
}
