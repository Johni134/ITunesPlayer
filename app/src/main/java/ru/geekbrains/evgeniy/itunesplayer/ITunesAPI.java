package ru.geekbrains.evgeniy.itunesplayer;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Korotchenko-es on 22.03.18.
 */

public interface ITunesAPI {
    @GET("search")
    Flowable<ModelResponse> getData(@Query("term") String searchTerm, @Query("media") String mediaType);
}
