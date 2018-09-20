package com.universetelecom.mvvm_users.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.universetelecom.mvvm_users.component.DaggerGiphyComponent;
import com.universetelecom.mvvm_users.component.GiphyComponent;
import com.universetelecom.mvvm_users.module.ContextModule;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Ahmad Shubita on 8/1/17.
 */

public class AppController extends Application {

    private GiphyComponent giphyComponent;

    private Scheduler scheduler;

    public static AppController get(Activity activity){
        return (AppController) activity.getApplication();
    }

    private static AppController get(Context context) {
        return (AppController) context.getApplicationContext();
    }

    public static AppController create(Context context) {
        return AppController.get(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        giphyComponent = DaggerGiphyComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
    }

    public GiphyComponent getGiphyComponent(){
        return giphyComponent;
    }

    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }

        return scheduler;
    }


    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

}
