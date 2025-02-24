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
import com.example.mealplanner.database.LocalDataSource;
import com.example.mealplanner.favorites.presenter.FavoritePresenter;
import com.example.mealplanner.favorites.presenter.FavoritePresenterImpl;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.network.RemoteDataSource;
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

        setupUI(view);
        initializePresenter();
        presenter.loadFavorites();

        return view;
    }

    private void setupUI(View view) {
        emptyFavoritesText = view.findViewById(R.id.empty_favorites_text);
        recyclerView = view.findViewById(R.id.recycler_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initializePresenter();
        adapter = new FavoritesAdapter(presenter);
        recyclerView.setAdapter(adapter);
    }

    private void initializePresenter() {
        MealRepository repository = MealRepository.getInstance(
                RemoteDataSource.getInstance(),
                LocalDataSource.getInstance(requireContext())
        );

        presenter = new FavoritePresenterImpl(this, repository);
    }

    @Override
    public void showFavorites(List<FavoriteMeal> meals) {
        if (meals.isEmpty()) {
            toggleEmptyState(true);
        } else {
            toggleEmptyState(false);
            adapter.setFavorites(meals);
        }
    }

    private void toggleEmptyState(boolean isEmpty) {
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        emptyFavoritesText.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFavoriteAdded(FavoriteMeal meal) {
        Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
        presenter.loadFavorites();
    }

    @Override
    public void showFavoriteRemoved(FavoriteMeal meal) {
        Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
        presenter.loadFavorites();
    }

    @Override
    public void onFavoriteChecked(boolean isFavorite) {
        // Handle UI updates if needed
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
