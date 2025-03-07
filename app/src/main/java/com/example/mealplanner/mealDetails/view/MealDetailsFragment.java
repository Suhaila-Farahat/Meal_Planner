package com.example.mealplanner.mealDetails.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.SessionManager;
import com.example.mealplanner.auth.login.view.LoginActivity;
import com.example.mealplanner.database.LocalDataSource;
import com.example.mealplanner.mealDetails.presenter.MealDetailsPresenter;
import com.example.mealplanner.mealDetails.presenter.MealDetailsPresenterImpl;
import com.example.mealplanner.mealplanning.model.PlannedMeal;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.models.mealModel.Ingredient;
import com.example.mealplanner.models.mealModel.MealDetails;
import com.example.mealplanner.network.RemoteDataSource;
import com.example.mealplanner.start.view.StartActivity;

import java.util.Calendar;
import java.util.List;

public class MealDetailsFragment extends Fragment implements MealDetailsView {

    private MealDetailsPresenter mealDetailsPresenter;
    private TextView mealName, mealArea, mealInstructions, noVideoText;
    private ImageView mealImage;
    private WebView videoWebView;
    private RecyclerView ingredientRecyclerView;
    private ProgressBar progressBar;
    private Button addToCalendar;
    private String mealId;
    private String mealImageUrl;
    private String mealTitle;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_details, container, false);
        initializeViews(view);
        setupPresenter();
        loadMealDetails();
        setupListeners();
        return view;
    }

    private void initializeViews(View view) {
        mealName = view.findViewById(R.id.mealName);
        mealArea = view.findViewById(R.id.mealArea);
        mealInstructions = view.findViewById(R.id.mealInstructions);
        mealImage = view.findViewById(R.id.mealImage);
        videoWebView = view.findViewById(R.id.videoWebView);
        noVideoText = view.findViewById(R.id.noVideoText);
        ingredientRecyclerView = view.findViewById(R.id.ingredientRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        addToCalendar = view.findViewById(R.id.addtocalender);

        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        sessionManager = new SessionManager(getContext());
    }

    private void setupPresenter() {
        mealDetailsPresenter = new MealDetailsPresenterImpl(
                this,
                MealRepository.getInstance(
                        RemoteDataSource.getInstance(),
                        LocalDataSource.getInstance(getContext())
                )
        );
    }

    private void loadMealDetails() {
        if (getArguments() != null) {
            mealId = getArguments().getString("mealId");
        }
        if (mealId != null) {
            mealDetailsPresenter.fetchMealDetails(mealId);
        } else {
            showError("Meal ID is missing");
        }
    }

    private void setupListeners() {
        addToCalendar.setOnClickListener(v -> {
            if (sessionManager.isGuest()) {
                showSignUpDialog();
            } else {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, month1, dayOfMonth1) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, month1, dayOfMonth1);

                    saveMealToCalendar(selectedDate.getTimeInMillis());
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());


        datePickerDialog.show();
    }

    private void saveMealToCalendar(long plannedDate) {
        if (mealId != null) {
            PlannedMeal plannedMeal = new PlannedMeal();
            plannedMeal.setMealId(mealId);
            plannedMeal.setMealName(mealTitle);
            plannedMeal.setMealImage(mealImageUrl);
            plannedMeal.setPlannedDate(plannedDate);

            mealDetailsPresenter.scheduleMeal(plannedMeal);

            showMessage("Meal added to calendar!");
        }
    }

    @Override
    public void showMealDetails(MealDetails mealDetails) {
        mealTitle = mealDetails.getStrMeal();
        mealImageUrl = mealDetails.getStrMealThumb();

        mealName.setText(mealTitle);
        mealArea.setText(mealDetails.getStrArea());
        mealInstructions.setText(mealDetails.getStrInstructions());
        Glide.with(this).load(mealImageUrl).into(mealImage);

        setupIngredientAdapter(mealDetails.getIngredients());
        handleVideoPlayback(mealDetails.getStrYoutube());
    }

    private void setupIngredientAdapter(List<Ingredient> ingredients) {
        IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext(), ingredients);
        ingredientRecyclerView.setAdapter(ingredientAdapter);
    }

    private void handleVideoPlayback(String youtubeUrl) {
        if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
            String videoId = extractYoutubeId(youtubeUrl);
            if (videoId != null) {
                videoWebView.setVisibility(View.VISIBLE);
                noVideoText.setVisibility(View.GONE);
                loadYoutubeVideo(videoId);
            } else {
                showNoVideoAvailable();
            }
        } else {
            showNoVideoAvailable();
        }
    }

    private void showNoVideoAvailable() {
        videoWebView.setVisibility(View.GONE);
        noVideoText.setVisibility(View.VISIBLE);
    }

    private void loadYoutubeVideo(String videoId) {
        String embedHtml = "<html><body style='margin:0;padding:0;'><iframe width='100%' height='100%' src='https://www.youtube.com/embed/"
                + videoId + "' frameborder='0' allowfullscreen></iframe></body></html>";

        WebSettings webSettings = videoWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        videoWebView.setWebViewClient(new WebViewClient());
        videoWebView.loadData(embedHtml, "text/html", "utf-8");
    }

    private String extractYoutubeId(String url) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
        java.util.regex.Pattern compiledPattern = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = compiledPattern.matcher(url);
        return matcher.find() ? matcher.group() : null;
    }

    private void showSignUpDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage("Only registered users can add meals to the Calendar")
                .setPositiveButton("Sign Up", (dialog, which) -> {
                    Intent intent = new Intent(getContext(), StartActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mealDetailsPresenter.onDestroy();
    }
}
