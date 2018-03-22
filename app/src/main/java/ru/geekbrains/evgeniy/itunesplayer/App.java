package ru.geekbrains.evgeniy.itunesplayer;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Korotchenko-es on 22.03.18.
 */

public class App extends Application {
    private static ITunesAPI iTunesAPI;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://itunes.apple.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        iTunesAPI = retrofit.create(ITunesAPI.class);
    }

    public static ITunesAPI getApi() {
        return iTunesAPI;
    }
}
