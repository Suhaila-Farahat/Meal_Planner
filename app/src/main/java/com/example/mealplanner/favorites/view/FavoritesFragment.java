package com.example.mealplanner.favorites.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealplanner.R;
import com.example.mealplanner.database.AppDatabase;
import com.example.mealplanner.database.FavoriteMeal;
import com.example.mealplanner.favorites.presenter.FavoritePresenter;
import com.example.mealplanner.favorites.presenter.FavoritePresenterImpl;
import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesView {
    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private TextView emptyFavoritesText;
    private FavoritePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        emptyFavoritesText = view.findViewById(R.id.empty_favorites_text);
        recyclerView = view.findViewById(R.id.recycler_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        presenter = new FavoritePresenterImpl(
                this,
                AppDatabase.getInstance(getContext()).favoriteMealDao()
        );

        adapter = new FavoritesAdapter(presenter);
        recyclerView.setAdapter(adapter);

        presenter.loadFavorites();

        return view;
    }

    @Override
    public void showFavorites(List<FavoriteMeal> meals) {
        if (meals.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyFavoritesText.setVisibility(View.VISIBLE);
        } else {
            adapter.setFavorites(meals);
            recyclerView.setVisibility(View.VISIBLE);
            emptyFavoritesText.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFavoriteAdded(FavoriteMeal meal) {
        Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
        presenter.loadFavorites();
    }

    @Override
    public void showFavoriteRemoved(FavoriteMeal meal) {
        Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
        presenter.loadFavorites();
    }

    @Override
    public void onFavoriteChecked(boolean isFavorite) {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}