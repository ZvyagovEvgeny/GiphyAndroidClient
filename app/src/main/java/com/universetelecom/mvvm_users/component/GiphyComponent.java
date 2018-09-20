package com.universetelecom.mvvm_users.component;

import com.universetelecom.mvvm_users.module.GiphyImagesModule;
import com.universetelecom.mvvm_users.network.GiphyService;
import com.universetelecom.mvvm_users.scope.GiphyImagesApplicationScope;

import dagger.Component;

@GiphyImagesApplicationScope
@Component(modules = {GiphyImagesModule.class})
public interface GiphyComponent {
    GiphyService getGiphyImagesService();
}
