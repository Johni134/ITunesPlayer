package ru.geekbrains.evgeniy.itunesplayer;

import android.app.Application;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Korotchenko-es on 22.03.18.
 */

public class App extends Application {
    private static ITunesAPI iTunesAPI;
    private Retrofit retrofit;

    private final static String API_ITUNES_BASE_URL = "https://itunes.apple.com";

    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl(API_ITUNES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        iTunesAPI = retrofit.create(ITunesAPI.class);
    }

    public static ITunesAPI getApi() {
        return iTunesAPI;
    }
}
