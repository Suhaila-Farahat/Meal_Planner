package com.example.mealplanner.Ingredient.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.SessionManager;
import com.example.mealplanner.auth.login.view.LoginActivity;
import com.example.mealplanner.auth.signup.view.SignUpActivity;
import com.example.mealplanner.database.FavoriteMeal;
import com.example.mealplanner.database.FavoriteMealDao;
import com.example.mealplanner.models.mealModel.Meal;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {
    private List<Meal> meals;
    private OnMealClickListener listener;
    private SessionManager sessionManager;
    private FavoriteMealDao favoriteMealDao;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Context context;

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    public MealAdapter(Context context, List<Meal> meals, OnMealClickListener listener, FavoriteMealDao favoriteMealDao) {
        this.context = context;
        this.meals = meals;
        this.listener = listener;
        this.favoriteMealDao = favoriteMealDao;
        this.sessionManager = new SessionManager(context);
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

        disposable.add(favoriteMealDao.isMealFavorite(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isFavorite -> {
                    holder.favButton.setImageResource(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
                }));

        holder.itemView.setOnClickListener(v -> {
            if (sessionManager.isGuest()) {
                showSignUpDialog();
            } else if (listener != null) {
                listener.onMealClick(meal);
            }
        });

        holder.favButton.setOnClickListener(v -> {
            if (sessionManager.isGuest()) {
                showSignUpDialog();
            } else {
                toggleFavorite(meal, holder);
            }
        });
    }

    private void toggleFavorite(Meal meal, ViewHolder holder) {
        disposable.add(favoriteMealDao.isMealFavorite(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isFavorite -> {
                    if (isFavorite) {
                        removeMealFromFavorites(meal);
                        holder.favButton.setImageResource(R.drawable.ic_favorite_border);
                    } else {
                        addMealToFavorites(meal);
                        holder.favButton.setImageResource(R.drawable.ic_favorite);
                    }
                }));
    }

    private void addMealToFavorites(Meal meal) {
        FavoriteMeal favoriteMeal = new FavoriteMeal(meal.getId(), meal.getName(), meal.getImageUrl());
        disposable.add(favoriteMealDao.insertFavorite(favoriteMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    private void removeMealFromFavorites(Meal meal) {
        disposable.add(favoriteMealDao.deleteFavorite(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    private void showSignUpDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Sign Up Required")
                .setMessage("Only registered users can add to favorites. Sign up now!")
                .setPositiveButton("Login", (dialog, which) -> {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        ImageView mealImage;
        ImageButton favButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealcard_name);
            mealImage = itemView.findViewById(R.id.mealcard_image);
            favButton = itemView.findViewById(R.id.mealcard_fav_button);
        }
    }
}
