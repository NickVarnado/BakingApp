package com.nbvarnado.bakingapp.data.network;

import android.content.Context;
import android.util.Log;

import com.nbvarnado.bakingapp.AppExecutors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeDataSource {

    private static final String TAG = RecipeDataSource.class.getSimpleName();

    private static RecipeDataSource sInstance;
    private RecipeDbService service;

    private Context mContext;
    private AppExecutors mExecutors;

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private RecipeDataSource(Context context, AppExecutors executors) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RecipeDbService.class);
        mContext = context;
        mExecutors = executors;
    }

    public static synchronized RecipeDataSource getInstance(Context context, AppExecutors executors) {
        if (sInstance == null) {
            Log.d(TAG, "Creating Recipe Data Source Instance");
            sInstance = new RecipeDataSource(context, executors);
        }
        Log.d(TAG, "Retrieving Recipe Data Source Instance");
        return sInstance;
    }

    public RecipeDbService getService(Context context, AppExecutors executors) {
        if (service == null) {
            getInstance(context, executors);
        }
        return service;
    }
}
