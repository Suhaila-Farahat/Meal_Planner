package com.example.mealplanner.countries.view;

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
import com.example.mealplanner.R;
import com.example.mealplanner.database.FavoriteMeal;
import com.example.mealplanner.database.FavoriteMealDao;
import com.example.mealplanner.models.mealModel.Meal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CountryMealListAdapter extends RecyclerView.Adapter<CountryMealListAdapter.MealViewHolder> {
    private Context context;
    private List<Meal> mealList = new ArrayList<>();
    private OnMealClickListener mealClickListener;
    private FavoriteMealDao favoriteMealDao;
    private CompositeDisposable disposable = new CompositeDisposable();

    public CountryMealListAdapter(Context context, OnMealClickListener mealClickListener, FavoriteMealDao favoriteMealDao) {
        this.context = context;
        this.mealClickListener = mealClickListener;
        this.favoriteMealDao = favoriteMealDao;
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
        Glide.with(context).load(meal.getImageUrl()).into(holder.mealImage);

        disposable.add(favoriteMealDao.isMealFavorite(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isFavorite -> {
                    holder.favButton.setImageResource(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
                }));

        holder.favButton.setOnClickListener(v -> {
            if (holder.favButton.getDrawable().getConstantState().equals(context.getDrawable(R.drawable.ic_favorite).getConstantState())) {
                removeMealFromFavorites(meal);
            } else {
                addMealToFavorites(meal);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (mealClickListener != null) {
                mealClickListener.onMealClick(meal.getId());
            }
        });
    }

    private void addMealToFavorites(Meal meal) {
        FavoriteMeal favoriteMeal = new FavoriteMeal(meal.getId(), meal.getName(), meal.getImageUrl());
        disposable.add(favoriteMealDao.insertFavorite(favoriteMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> notifyDataSetChanged()));
    }

    private void removeMealFromFavorites(Meal meal) {
        disposable.add(favoriteMealDao.deleteFavorite(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> notifyDataSetChanged()));
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        ImageView mealImage;
        ImageButton favButton;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealcard_name);
            mealImage = itemView.findViewById(R.id.mealcard_image);
            favButton = itemView.findViewById(R.id.mealcard_fav_button);
        }
    }

    public interface OnMealClickListener {
        void onMealClick(String mealId);
    }
}
