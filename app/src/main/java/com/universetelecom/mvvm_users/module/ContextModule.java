package com.universetelecom.mvvm_users.module;

import android.content.Context;

import com.universetelecom.mvvm_users.module.context.ApplicationContext;
import com.universetelecom.mvvm_users.scope.GiphyImagesApplicationScope;

import dagger.Module;
import dagger.Provides;


@Module
public class ContextModule {

    Context context;

    public ContextModule(Context context){
        this.context = context;
    }

    @ApplicationContext
    @GiphyImagesApplicationScope
    @Provides
    public Context context(){ return context.getApplicationContext(); }
}