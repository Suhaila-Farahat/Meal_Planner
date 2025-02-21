package com.example.mealplanner.home.view.countries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealplanner.R;
import com.example.mealplanner.models.flagsModel.CountryFlag;

import java.util.List;

public class CountryFlagAdapter extends RecyclerView.Adapter<CountryFlagAdapter.ViewHolder> {
    private List<CountryFlag> countryFlags;
    private Context context;

    public CountryFlagAdapter(List<CountryFlag> countryFlags, Context context) {
        this.countryFlags = countryFlags;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_country_flag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CountryFlag countryFlag = countryFlags.get(position);
        holder.countryName.setText(countryFlag.getCountryName());
        holder.flagImage.setImageResource(countryFlag.getFlagResId());
    }

    @Override
    public int getItemCount() {

        return countryFlags.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView flagImage;
        TextView countryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flagImage = itemView.findViewById(R.id.flag_image);
            countryName = itemView.findViewById(R.id.country_name);
        }
    }
}
