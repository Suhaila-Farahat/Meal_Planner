package com.example.mealplanner.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealplanner.R;
import com.bumptech.glide.Glide;
import com.example.mealplanner.models.flagsModel.CountryFlag;

import java.util.List;

public class CountryFlagAdapter extends RecyclerView.Adapter<CountryFlagAdapter.CountryFlagViewHolder> {

    private Context context;
    private List<CountryFlag> flags;
    private OnFlagClickListener flagClickListener;

    public interface OnFlagClickListener {
        void onFlagClick(String countryName);
    }

    public CountryFlagAdapter(Context context, List<CountryFlag> flags, OnFlagClickListener flagClickListener) {
        this.context = context;
        this.flags = flags;
        this.flagClickListener = flagClickListener;
    }

    @Override
    public CountryFlagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_country_flag, parent, false);
        return new CountryFlagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryFlagViewHolder holder, int position) {
        CountryFlag flag = flags.get(position);
        holder.flagName.setText(flag.getCountryName());
        Glide.with(context)
                .load(flag.getFlagResId())
                .into(holder.flagImage);

        holder.itemView.setOnClickListener(v -> {
            flagClickListener.onFlagClick(flag.getCountryName());
        });
    }

    @Override
    public int getItemCount() {
        return flags.size();
    }

    public static class CountryFlagViewHolder extends RecyclerView.ViewHolder {
        TextView flagName;
        ImageView flagImage;

        public CountryFlagViewHolder(View itemView) {
            super(itemView);
            flagName = itemView.findViewById(R.id.country_name);
            flagImage = itemView.findViewById(R.id.flag_image);
        }
    }
}
