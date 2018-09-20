package com.universetelecom.mvvm_users.view.activity.loader;

import android.content.Context;
import android.support.v4.content.Loader;
import android.util.Log;

import com.universetelecom.mvvm_users.viewModel.base.ViewModel;
import com.universetelecom.mvvm_users.viewModel.base.ViewModelFactory;


public class ViewModelLoader<T extends ViewModel> extends Loader<T> {

    private final ViewModelFactory<T> factory;
    private final String tag;
    private T viewModel;
    static private String LOG_TAG ="PresenterLoader";

    public ViewModelLoader(Context context, ViewModelFactory<T> factory, String tag) {
        super(context);
        this.factory = factory;
        this.tag = tag;
    }


    //При старте активити
    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "onStartLoading-" + tag);

        // if we already own a presenter instance, simply deliver it.
        if (viewModel != null) {
            deliverResult(viewModel);
            return;
        }

        // Otherwise, force a load
        forceLoad();
    }

    //Работа лоадера, вызывается при forceLoad()
    @Override
    protected void onForceLoad() {
        Log.i(LOG_TAG, "onForceLoad-" + tag);

        // Create the Presenter using the Factory
        viewModel = factory.create();

        // Deliver the result
        deliverResult(viewModel);
    }

    @Override
    public void deliverResult(T data) {
        super.deliverResult(data);
        Log.i(LOG_TAG, "deliverResult-" + tag);
    }

    //При остановке активити
    @Override
    protected void onStopLoading() {
        Log.i(LOG_TAG, "onStopLoading-" + tag);
    }

    @Override
    protected void onReset() {
        Log.i(LOG_TAG, "onReset-" + tag);
        if (viewModel != null) {
            viewModel.onDestroyed();
            viewModel = null;
        }
    }

    public T getPresenter() {
        return viewModel;
    }
}
