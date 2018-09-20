package com.universetelecom.mvvm_users.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.universetelecom.mvvm_users.network.GiphyService;
import com.universetelecom.mvvm_users.scope.GiphyImagesApplicationScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module(includes = OkHttpClientModule.class)
public class GiphyImagesModule {

    @Provides
    public GiphyService gipyServiceApi(Retrofit retrofit){
        return retrofit.create(GiphyService.class);
    }

    @GiphyImagesApplicationScope
    @Provides
    public Retrofit retrofit(OkHttpClient okHttpClient){

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl( GiphyService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }
}
