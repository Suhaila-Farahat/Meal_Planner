package com.example.mealplanner.home.view.countries;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealplanner.R;
import com.example.mealplanner.models.flagsModel.CountryFlag;
import com.example.mealplanner.util.FlagUtils;

import java.util.List;

public class AllFlagsFragment extends Fragment {
    private RecyclerView flagRecyclerView;
    private CountryFlagAdapter adapter;
    private List<CountryFlag> countryFlags;

    public AllFlagsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_flags, container, false);

        flagRecyclerView = view.findViewById(R.id.all_flags_recycler_view);
        countryFlags = FlagUtils.getCountryFlags();

        adapter = new CountryFlagAdapter(countryFlags, getContext());
        flagRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        flagRecyclerView.setAdapter(adapter);
        flagRecyclerView.setHasFixedSize(true);


        return view;
    }
}
