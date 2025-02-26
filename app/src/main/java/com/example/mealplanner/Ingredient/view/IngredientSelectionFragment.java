package com.example.mealplanner.Ingredient.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealplanner.Ingredient.presenter.IngredientSelectionPresenter;
import com.example.mealplanner.Ingredient.presenter.IngredientSelectionPresenterImpl;
import com.example.mealplanner.R;
import com.example.mealplanner.database.LocalDataSource;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.models.mealModel.Ingredient;
import com.example.mealplanner.network.RemoteDataSource;
import java.util.List;

public class IngredientSelectionFragment extends Fragment implements IngredientSelectionView {

    private RecyclerView ingredientRecyclerView;
    private IngredientSelectionPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_selection, container, false);
        initializeViews(view);
        setupPresenter();
        presenter.fetchIngredients();
        return view;
    }

    private void initializeViews(View view) {
        ingredientRecyclerView = view.findViewById(R.id.ingredientRecyclerView);
        ingredientRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    private void setupPresenter() {
        MealRepository mealRepository = MealRepository.getInstance(
                RemoteDataSource.getInstance(),
                LocalDataSource.getInstance(getContext())
        );
        presenter = new IngredientSelectionPresenterImpl(this, mealRepository);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        IngredientsCardsAdapter adapter = new IngredientsCardsAdapter(ingredients, this::navigateToMealList);
        ingredientRecyclerView.setAdapter(adapter);
    }

    private void navigateToMealList(Ingredient ingredient) {
        Bundle bundle = new Bundle();
        bundle.putString("ingredientName", ingredient.getName());

        Fragment mealListFragment = new MealCardsListFragment();
        mealListFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mealListFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
