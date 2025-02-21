package com.example.mealplanner.countries.view;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.List;

public class CountryMealListAdapter extends RecyclerView.Adapter<CountryMealListAdapter.MealViewHolder> {
    private Context context;
    private List<Meal> mealList = new ArrayList<>();
    private OnMealClickListener mealClickListener;

    public CountryMealListAdapter(Context context, OnMealClickListener mealClickListener) {
        this.context = context;
        this.mealClickListener = mealClickListener;
    }

    public void setMeals(List<Meal> meals) {
        this.mealList = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.mealName.setText(meal.getName());
        Glide.with(context)
                .load(meal.getImageUrl())
                .into(holder.mealImage);

        holder.itemView.setOnClickListener(v -> {
            if (mealClickListener != null) {
                mealClickListener.onMealClick(meal.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        ImageView mealImage;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealcard_name);
            mealImage = itemView.findViewById(R.id.mealcard_image);
        }
    }

    public interface OnMealClickListener {
        void onMealClick(String mealId);
    }
}
