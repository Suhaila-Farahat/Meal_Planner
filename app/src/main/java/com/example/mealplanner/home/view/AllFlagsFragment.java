package com.example.mealplanner.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealplanner.R;
import com.example.mealplanner.countries.view.CountryMealListFragment;
import com.example.mealplanner.models.flagsModel.CountryFlag;
import com.example.mealplanner.util.FlagUtils;

import java.util.List;

public class AllFlagsFragment extends Fragment {

    private RecyclerView flagRecyclerView;

    public static AllFlagsFragment newInstance(List<CountryFlag> flags) {
        AllFlagsFragment fragment = new AllFlagsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_flags, container, false);
        flagRecyclerView = view.findViewById(R.id.all_flags_recycler_view);
        List<CountryFlag> flags = FlagUtils.getCountryFlags();
        showFlags(flags);
        return view;
    }

    private void showFlags(List<CountryFlag> flags) {
        CountryFlagAdapter flagAdapter = new CountryFlagAdapter(getContext(), flags, countryName -> {
            String countryAPIName = FlagUtils.getCountryAPIName(countryName);

            CountryMealListFragment fragment = new CountryMealListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("country_name", countryAPIName);
            fragment.setArguments(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        flagRecyclerView.setLayoutManager(gridLayoutManager);
        flagRecyclerView.setAdapter(flagAdapter);
    }
}
