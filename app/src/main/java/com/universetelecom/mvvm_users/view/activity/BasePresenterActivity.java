package com.universetelecom.mvvm_users.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.universetelecom.mvvm_users.view.activity.loader.ViewModelLoader;
import com.universetelecom.mvvm_users.viewModel.base.ViewModel;
import com.universetelecom.mvvm_users.viewModel.base.ViewModelFactory;


public abstract class BasePresenterActivity<P extends ViewModel<V>,V> extends AppCompatActivity {

    private static final int LOADER_ID = 101;
    private static String LOG_TAG = "BasePresenterActivity";
    private P presenter;

    private int loaderId(){
        return LOADER_ID;
    }

    static void log(String d,String method){
        Log.d(LOG_TAG,d+": "+method);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log("onCreate","Presenter");
        super.onCreate(savedInstanceState);

        Loader<P> loader = LoaderManager.getInstance(this).getLoader(loaderId());
        if (loader == null) {
            initLoader();
        } else {
            this.presenter = ((ViewModelLoader<P>) loader).getPresenter();
            onPresenterCreatedOrRestored(presenter);
        }
    }

    private void initLoader() {

        LoaderManager.getInstance(this).initLoader(loaderId(),null,callbacks);
    }

    LoaderManager.LoaderCallbacks<P> callbacks = new LoaderManager.LoaderCallbacks<P>(){

        @Override
        public final Loader<P> onCreateLoader(int id, Bundle args) {
            Log.i(LOG_TAG, "onCreateLoader");
            return new ViewModelLoader<>(BasePresenterActivity.this,
                    getPresenterFactory(), tag());
        }

        @Override
        public final void onLoadFinished(Loader<P> loader, P presenter) {
            Log.i(LOG_TAG, "onLoadFinished");
            BasePresenterActivity.this.presenter = presenter;;
            onPresenterCreatedOrRestored(presenter);
        }

        @Override
        public final void onLoaderReset(Loader<P> loader) {
            Log.i(LOG_TAG, "onLoaderReset");
            BasePresenterActivity.this.presenter = null;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart-" + tag());

    }

    @Override
    protected void onStop() {

        super.onStop();
        presenter.onDestroyed();
        Log.i(LOG_TAG, "onStop-" + tag());
    }


    /**
     * String tag use for log purposes.
     */
    @NonNull
    protected abstract String tag();

    /**
     * Instance of {@link ViewModelFactory} use to create a Presenter when needed. This instance should
     * not contain {@link android.app.Activity} context reference since it will be keep on rotations.
     */
    @NonNull
    protected abstract ViewModelFactory<P> getPresenterFactory();

    /**
     * Hook for subclasses that deliver the {@link ViewModel} before its View is attached.
     * Can be use to initialize the Presenter or simple hold a reference to it.
     */
    protected abstract void onPresenterCreatedOrRestored(@NonNull P presenter);
}
