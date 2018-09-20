package com.universetelecom.mvvm_users.module;

import android.app.Activity;
import android.content.Context;


import com.universetelecom.mvvm_users.module.context.ActivityContext;
import com.universetelecom.mvvm_users.scope.GiphyImagesApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Context context;

    ActivityModule(Activity context){
        this.context = context;
    }

    @ActivityContext
    @GiphyImagesApplicationScope
    @Provides
    public Context context(){ return context; }

}
