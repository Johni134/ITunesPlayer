package ru.geekbrains.evgeniy.itunesplayer;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Korotchenko-es on 22.03.18.
 */

public interface ITunesAPI {
    @GET("search")
    Call<ModelResponse> getData(@Query("term") String searchTerm, @Query("media") String mediaType);
}
