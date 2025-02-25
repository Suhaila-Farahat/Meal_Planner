package com.example.mealplanner.mealplanning.view;

import com.example.mealplanner.mealplanning.model.PlannedMeal;
import java.util.List;

public interface CalendarView {
    void showPlannedMeals(List<PlannedMeal> plannedMeals);
    void showError(String message);
}
