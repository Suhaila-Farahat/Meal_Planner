package com.example.mealplanner.mealDetails.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.example.mealplanner.database.AppDatabase;
import com.example.mealplanner.database.LocalDataSource;
import com.example.mealplanner.mealDetails.presenter.MealDetailsPresenter;
import com.example.mealplanner.mealDetails.presenter.MealDetailsPresenterImpl;
import com.example.mealplanner.models.MealRepository;
import com.example.mealplanner.models.mealModel.Ingredient;
import com.example.mealplanner.models.mealModel.MealDetails;
import com.example.mealplanner.network.RemoteDataSource;
import java.util.List;

public class MealDetailsFragment extends Fragment implements MealDetailsView {

    private MealDetailsPresenter mealDetailsPresenter;
    private TextView mealName, mealArea, mealInstructions, noVideoText;
    private ImageView mealImage;
    private WebView videoWebView;
    private RecyclerView ingredientRecyclerView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_details, container, false);
        initializeViews(view);
        setupPresenter();
        loadMealDetails();
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

        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
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
        String mealId = getArguments() != null ? getArguments().getString("mealId") : null;
        if (mealId != null) {
            mealDetailsPresenter.fetchMealDetails(mealId);
        } else {
            showError("Meal ID is missing");
        }
    }

    @Override
    public void showMealDetails(MealDetails mealDetails) {
        hideLoading();
        mealName.setText(mealDetails.getStrMeal());
        mealArea.setText(mealDetails.getStrArea());
        mealInstructions.setText(mealDetails.getStrInstructions());
        Glide.with(this).load(mealDetails.getStrMealThumb()).into(mealImage);

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

    @Override
    public void showError(String message) {
        hideLoading();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mealDetailsPresenter.onDestroy();
    }
}
