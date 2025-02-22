package com.example.mealplanner.mealDetails.view;

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
import com.example.mealplanner.models.mealModel.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private Context context;
    private List<Ingredient> ingredientList;

    public IngredientAdapter(Context context, List<Ingredient> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingredient_q, parent, false);
        return new IngredientViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);

        if (holder.ingredientName != null) {
            holder.ingredientName.setText(ingredient.getName());
        }

        if (holder.ingredientMeasurement != null) {
            holder.ingredientMeasurement.setText(""+ ingredient.getMeasurement());
        }

        Glide.with(context).load(ingredient.getImageUrl()).into(holder.ingredientImage);
    }


    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName, ingredientMeasurement;
        ImageView ingredientImage;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name_q);
            ingredientImage = itemView.findViewById(R.id.ingredient_image_q);
            ingredientMeasurement = itemView.findViewById(R.id.ingredient_quantity_q);
        }
    }
}
