package com.universetelecom.mvvm_users.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class GiphyApiFactory {

    public static GiphyService create() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(GiphyService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(GiphyService.class);
    }

}
