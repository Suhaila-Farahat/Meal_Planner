package com.example.mealplanner.categories.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.mealplanner.SessionManager;
import com.example.mealplanner.auth.login.view.LoginActivity;
import com.example.mealplanner.database.FavoriteMeal;
import com.example.mealplanner.database.FavoriteMealDao;
import com.example.mealplanner.models.mealModel.Meal;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MealViewHolder> {
    private List<Meal> mealList = new ArrayList<>();
    private OnMealClickListener listener;
    private FavoriteMealDao favoriteMealDao;
    private OnFavoriteChangedListener favoriteChangedListener;
    private CompositeDisposable disposable = new CompositeDisposable();
    private SessionManager sessionManager;
    private Context context;

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    public interface OnFavoriteChangedListener {
        void onFavoriteChanged();
    }

    public MealListAdapter(Context context, OnMealClickListener listener, FavoriteMealDao favoriteMealDao, OnFavoriteChangedListener favoriteChangedListener) {
        this.context = context;
        this.listener = listener;
        this.favoriteMealDao = favoriteMealDao;
        this.favoriteChangedListener = favoriteChangedListener;
        this.sessionManager = new SessionManager(context);
    }

    public void setMeals(List<Meal> meals) {
        this.mealList = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.mealName.setText(meal.getName());

        Glide.with(holder.itemView.getContext())
                .load(meal.getImageUrl())
                .into(holder.mealImage);

        disposable.add(favoriteMealDao.isMealFavorite(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    boolean isFavorite = count;
                    holder.favButton.setImageResource(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
                }, Throwable::printStackTrace));

        holder.favButton.setOnClickListener(v -> {
            if (sessionManager.isGuest()) {
                showSignUpDialog();
                return;
            }

            disposable.add(favoriteMealDao.isMealFavorite(meal.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(count -> {
                        boolean isFavorite = count;
                        if (isFavorite) {
                            removeMealFromFavorites(meal, holder.favButton);
                        } else {
                            addMealToFavorites(meal, holder.favButton);
                        }
                    }, Throwable::printStackTrace));
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMealClick(meal);
            }
        });
    }

    private void showSignUpDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Sign Up Required")
                .setMessage("Sign up to save your favorite meals!")
                .setPositiveButton("Login", (dialog, which) -> {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void addMealToFavorites(Meal meal, ImageButton favoriteButton) {
        FavoriteMeal favoriteMeal = new FavoriteMeal(meal.getId(), meal.getName(), meal.getImageUrl());
        disposable.add(favoriteMealDao.insertFavorite(favoriteMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    favoriteButton.setImageResource(R.drawable.icons_heart);
                    if (favoriteChangedListener != null) {
                        favoriteChangedListener.onFavoriteChanged();
                    }
                }, Throwable::printStackTrace));
    }

    private void removeMealFromFavorites(Meal meal, ImageButton favoriteButton) {
        disposable.add(favoriteMealDao.deleteFavorite(meal.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_border);
                    if (favoriteChangedListener != null) {
                        favoriteChangedListener.onFavoriteChanged();
                    }
                }, Throwable::printStackTrace));
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
}
