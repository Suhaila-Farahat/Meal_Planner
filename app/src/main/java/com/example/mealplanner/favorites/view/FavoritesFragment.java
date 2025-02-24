package com.example.mealplanner.favorites.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealplanner.R;
import com.example.mealplanner.database.AppDatabase;
import com.example.mealplanner.database.FavoriteMealDao;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritesFragment extends Fragment {
    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private FavoriteMealDao favoriteMealDao;
    private TextView emptyFavoritesText;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        emptyFavoritesText =view.findViewById(R.id.empty_favorites_text);
        recyclerView = view.findViewById(R.id.recycler_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AppDatabase database = AppDatabase.getInstance(getContext());
        favoriteMealDao = database.favoriteMealDao();

        adapter = new FavoritesAdapter(favoriteMealDao);
        recyclerView.setAdapter(adapter);

        loadFavorites();

        return view;
    }

    private void loadFavorites() {
        disposable.add(favoriteMealDao.getAllFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favoriteMeals -> {
                    if (favoriteMeals.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyFavoritesText.setVisibility(View.VISIBLE);
                    } else {
                        adapter.setFavorites(favoriteMeals);
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyFavoritesText.setVisibility(View.GONE);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
