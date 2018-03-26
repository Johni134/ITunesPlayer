package ru.geekbrains.evgeniy.itunesplayer;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Korotchenko-es on 22.03.18.
 */

public interface ITunesAPI {
    @GET("search")
    Observable<ModelResponse> getData(@Query("term") String searchTerm, @Query("media") String mediaType);
}
