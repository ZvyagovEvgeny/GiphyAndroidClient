package com.universetelecom.mvvm_users.network;

import com.universetelecom.mvvm_users.model.giphy.ImagesResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.OPTIONS;
import retrofit2.http.Query;


public interface GiphyService {
    String BASE_URL = "https://api.giphy.com/v1/";
    String API_KEY = "W7nIee8VueTxbpnzvPUgZ6bKZi9Jjn8z";
    String RATING_PG_13 = "PG-13";



    @GET("gifs/search")
    Observable<ImagesResponse> fetchSearch(
            @Query("q") String search,
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("rating") String rating);

    @GET("gifs/trending")
    Observable<ImagesResponse> fetchTrendingUsers(
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("rating") String rating);
}
