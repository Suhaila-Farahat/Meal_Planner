package com.example.mealplanner.mealplanning.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealplanner.R;
import com.example.mealplanner.mealplanning.model.PlannedMeal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlannedMealsAdapter extends RecyclerView.Adapter<PlannedMealsAdapter.ViewHolder> {

    private final Context context;
    private final List<PlannedMeal> plannedMeals;
    private final OnMealDeleteListener onMealDeleteListener;

    public PlannedMealsAdapter(Context context, List<PlannedMeal> plannedMeals, OnMealDeleteListener onMealDeleteListener) {
        this.context = context;
        this.plannedMeals = plannedMeals;
        this.onMealDeleteListener = onMealDeleteListener;
    }

    public void updateMeals(List<PlannedMeal> newMeals) {
        plannedMeals.clear();
        plannedMeals.addAll(newMeals);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_planned_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlannedMeal meal = plannedMeals.get(position);
        holder.tvMealName.setText(meal.getMealName());

        String formattedDate = formatDate(meal.getPlannedDate());
        holder.tvMealDate.setText(formattedDate);

        Glide.with(context)
                .load(meal.getMealImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivMealImage);

        holder.btnRemoveMeal.setOnClickListener(v -> {
            if (onMealDeleteListener != null) {
                onMealDeleteListener.onMealDeleted(meal, position);
            }
        });
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }


    @Override
    public int getItemCount() {
        return plannedMeals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivMealImage;
        final TextView tvMealName, tvMealDate;
        final ImageButton btnRemoveMeal;

        ViewHolder(View itemView) {
            super(itemView);
            ivMealImage = itemView.findViewById(R.id.ivMealImage);
            tvMealName = itemView.findViewById(R.id.tvMealName);
            tvMealDate = itemView.findViewById(R.id.tvMealDate);
            btnRemoveMeal = itemView.findViewById(R.id.btnRemoveMeal);
        }
    }

    public interface OnMealDeleteListener {
        void onMealDeleted(PlannedMeal meal, int position);
    }
}
