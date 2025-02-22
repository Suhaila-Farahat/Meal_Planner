package com.example.mealplanner.Ingredient.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.models.mealModel.Meal;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {
    private List<Meal> meals;
    private OnMealClickListener listener;

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    public MealAdapter(List<Meal> meals, OnMealClickListener listener) {
        this.meals = meals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.mealName.setText(meal.getName());

        Glide.with(holder.itemView.getContext())
                .load(meal.getImageUrl())
                .into(holder.mealImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMealClick(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        ImageView mealImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealcard_name);
            mealImage = itemView.findViewById(R.id.mealcard_image);
        }
    }
}
