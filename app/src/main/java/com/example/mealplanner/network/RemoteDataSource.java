package com.example.mealplanner.network;

import com.example.mealplanner.models.mealModel.NetworkResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RemoteDataSource {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static volatile RemoteDataSource instance;
    private final MealApiService mealApiService;

    private RemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        mealApiService = retrofit.create(MealApiService.class);
    }

    public static RemoteDataSource getInstance() {
        if (instance == null) {
            synchronized (RemoteDataSource.class) {
                if (instance == null) {
                    instance = new RemoteDataSource();
                }
            }
        }
        return instance;
    }

    public Single<NetworkResponse> getRandomMeal() {
        return mealApiService.getRandomMeal();
    }

    public Single<NetworkResponse> getCategories() {
        return mealApiService.getCategories();
    }

    interface MealApiService {
        @GET("random.php")
        Single<NetworkResponse> getRandomMeal();

        @GET("categories.php")
        Single<NetworkResponse> getCategories();
    }
}
