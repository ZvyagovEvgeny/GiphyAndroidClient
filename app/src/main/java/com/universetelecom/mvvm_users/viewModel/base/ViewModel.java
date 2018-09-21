package com.universetelecom.mvvm_users.viewModel.base;

public interface ViewModel<V> {

    void onViewAttached(V view);

    void onDestroyed();

}
