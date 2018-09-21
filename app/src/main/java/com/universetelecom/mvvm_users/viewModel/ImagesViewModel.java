package com.universetelecom.mvvm_users.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.universetelecom.mvvm_users.R;
import com.universetelecom.mvvm_users.app.AppController;
import com.universetelecom.mvvm_users.component.GiphyComponent;
import com.universetelecom.mvvm_users.model.giphy.Datum;
import com.universetelecom.mvvm_users.model.giphy.ImagesResponse;
import com.universetelecom.mvvm_users.network.GiphyService;
import com.universetelecom.mvvm_users.view.activity.ActivityView;
import com.universetelecom.mvvm_users.viewModel.base.ViewModel;
import com.universetelecom.mvvm_users.BR;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ImagesViewModel extends BaseObservable implements ViewModel<ActivityView> {

    private final int COUNT_IMAGES_PER_PAGE = 20;
    private final String DEFAULT_RATING = GiphyService.RATING_PG_13;
    private final String INTERNET_ERROR_MESSAGE;
    private final String TRENDING_STR;
    private final String NO_RESULT;

    public ObservableBoolean imagesListVisibility;
    public ObservableBoolean messageLabelVisibility;
    public ObservableBoolean initialProgressBarVisibility;
    public ObservableField<String> messageLabel;
    public ObservableField<String> toolbarMessage;
    public ObservableBoolean allPagesDownloaded;
    public ObservableBoolean requestInProcess;

    private Context context;
    private List<Datum> data = new ArrayList<>();
    private ImagesResponse lastImagesResponse;
    private String currentSearchQuery;
    private RequestType currentRequestType = RequestType.TRENDING;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int currentPageNumber = 0;


    private enum RequestType {
        TRENDING, USER_SEARCH
    }

    private void cancelDataLoading(){
        compositeDisposable.dispose();
        compositeDisposable = new CompositeDisposable();
    }

    private void requestComplete() {
        requestInProcess.set(false);
    }

    public ImagesViewModel(@NonNull Context context) {
        this.context = context;
        INTERNET_ERROR_MESSAGE = context.getString(R.string.error_message_loading);
        TRENDING_STR = context.getString(R.string.trend_gifs);
        NO_RESULT = context.getString(R.string.no_result);

        initialProgressBarVisibility = new ObservableBoolean(false);
        imagesListVisibility = new ObservableBoolean(false);
        messageLabelVisibility = new ObservableBoolean(false);
        messageLabel = new ObservableField<>("");
        toolbarMessage = new ObservableField<>(TRENDING_STR);
        allPagesDownloaded = new ObservableBoolean(false);
        requestInProcess = new ObservableBoolean(false);
    }

    private boolean serverHaveMoreItems(){
        if(lastImagesResponse == null)return true;
        boolean allDataDownloaded = lastImagesResponse.getPagination().getTotalCount() <= data.size();
        if (allDataDownloaded){
            return false;
        }
        return true;
    }

    public void loadNextPage(){

        if(!serverHaveMoreItems()){
            allPagesDownloaded.set(true);
            return;
        }
        else {
            allPagesDownloaded.set(false);
        }

        if(requestInProcess.get())return;

        switch (currentRequestType){
            case TRENDING:
                loadNextPageOfTrends(currentPageNumber);
                break;

            case USER_SEARCH:
                loadNexPageOfSearch(currentPageNumber,currentSearchQuery);
                break;
        }
        requestInProcess.set(true);
        currentPageNumber++;
    }


    private void loadNexPageOfSearch(int page, String search){
        AppController appController = AppController.create(context);
        GiphyComponent giphyComponent = appController.getGiphyComponent();
        int offset = page * COUNT_IMAGES_PER_PAGE;

        GiphyService giphyService = giphyComponent.getGiphyImagesService();
        Disposable disposable = giphyService
                .fetchSearch(search,offset,COUNT_IMAGES_PER_PAGE,DEFAULT_RATING)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onImagesDownloaded,this::onImagesDownloadError);
        compositeDisposable.add(disposable);
    }

    private void loadNextPageOfTrends(int page){
        AppController appController = AppController.create(context);
        GiphyComponent giphyComponent = appController.getGiphyComponent();
        int offset = page * COUNT_IMAGES_PER_PAGE;

        GiphyService giphyService = giphyComponent.getGiphyImagesService();
        Disposable disposable = giphyService
                .fetchTrendingUsers(offset,COUNT_IMAGES_PER_PAGE,DEFAULT_RATING)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onImagesDownloaded,this::onImagesDownloadError);
        compositeDisposable.add(disposable);
    }

    private void onImagesDownloaded(ImagesResponse imagesResponse){
        Timber.d("onImagesDownloaded");

        requestComplete();

        data.addAll(imagesResponse.getData());
        lastImagesResponse = imagesResponse;
        if(data.size()==0){
            showMessage(NO_RESULT);
            return;
        }
        showImagesList();
        imagesListChanged();
    }

    private void onImagesDownloadError(Throwable throwable){
        showMessage(INTERNET_ERROR_MESSAGE);
        requestComplete();
        Timber.d(throwable.getMessage());
    }

    private void imagesListChanged(){
        notifyPropertyChanged(BR.data);
    }

    @Bindable
    public List<Datum> getData() {
        return this.data;
    }

    private void resetParams(){
        cancelDataLoading();
        currentPageNumber = 0;
        data.clear();
        allPagesDownloaded.set(false);
        lastImagesResponse = null;
        imagesListChanged();
    }

    private void setCurrentRequestType(RequestType requestType){
        currentRequestType = requestType;
    }

    private void resetVisibilityOfUI(){
        initialProgressBarVisibility.set(false);
        imagesListVisibility.set(false);
        messageLabelVisibility.set(false);
    }

    private void showMessage(String message){
        resetVisibilityOfUI();
        messageLabelVisibility.set(true);
        messageLabel.set(message);
    }

    private void showInitialProgressBar(){
        resetVisibilityOfUI();
        initialProgressBarVisibility.set(true);
    }

    private void showImagesList(){
        resetVisibilityOfUI();
        imagesListVisibility.set(true);
    }

    public List<Datum> getImagesList() {
        return data;
    }

    public void searchGIFs(String search){
        cancelDataLoading();
        resetParams();
        currentSearchQuery = search;
        setCurrentRequestType(RequestType.USER_SEARCH);
        toolbarMessage.set(search);
        loadNextPage();
    }

    public void showTrending(){
        cancelDataLoading();
        resetParams();
        resetVisibilityOfUI();

        setCurrentRequestType(RequestType.TRENDING);
        toolbarMessage.set(TRENDING_STR);
        loadNextPage();
    }

    @Override
    public void onViewAttached(ActivityView view) {
        context = view.getContext();
        //Initial download
        if(currentPageNumber==0){
            showInitialProgressBar();
            loadNextPage();
        }
    }

    @Override
    public void onDestroyed() {
        Timber.d("onDestroyed");
        context = null;
        compositeDisposable.dispose();
        compositeDisposable = new CompositeDisposable();
    }
}
