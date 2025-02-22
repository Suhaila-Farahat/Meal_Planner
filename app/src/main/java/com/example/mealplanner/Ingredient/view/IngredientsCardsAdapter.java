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
import com.example.mealplanner.models.mealModel.Ingredient;
import java.util.List;

public class IngredientsCardsAdapter extends RecyclerView.Adapter<IngredientsCardsAdapter.ViewHolder> {
    private List<Ingredient> ingredients;
    private OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
    }

    public IngredientsCardsAdapter(List<Ingredient> ingredients, OnIngredientClickListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.ingredientName.setText(ingredient.getName());

        Glide.with(holder.itemView.getContext())
                .load("https://www.themealdb.com/images/ingredients/" + ingredient.getName()+ "-Small.png")
                .into(holder.ingredientImage);

        holder.itemView.setOnClickListener(v -> listener.onIngredientClick(ingredient));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName;
        ImageView ingredientImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
            ingredientImage = itemView.findViewById(R.id.ingredient_image);
        }
    }
}
