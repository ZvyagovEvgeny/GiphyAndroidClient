package com.universetelecom.mvvm_users.view.activity;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;


import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.universetelecom.mvvm_users.R;

import com.universetelecom.mvvm_users.view.adapter.ImageAdapter;

import com.universetelecom.mvvm_users.databinding.*;
import com.universetelecom.mvvm_users.view.scrollListener.PaginationScrollListener;
import com.universetelecom.mvvm_users.viewModel.ImagesViewModel;

import com.universetelecom.mvvm_users.viewModel.base.ViewModelFactory;


public class ImagesActivity extends BasePresenterActivity<ImagesViewModel,ActivityView> implements ActivityView {
    private ActivityImagesListBinding activityImagesListBinding;
    private ImagesViewModel imagesViewModel;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initDataBinding() {
        activityImagesListBinding = DataBindingUtil.setContentView(this, R.layout.activity_images_list);
        activityImagesListBinding.setImagesViewModel(imagesViewModel);
    }

    // set up the list of user with recycler view
    private void setUpListOfUsersView(RecyclerView listImages) {

            ImageAdapter imageAdapter = new ImageAdapter();
            imageAdapter.updateData(imagesViewModel.getImagesList());
            listImages.setAdapter(imageAdapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            listImages.setLayoutManager(layoutManager);
            listImages.addOnScrollListener(new PaginationScrollListener(layoutManager) {

                @Override
                protected void loadMoreItems() {
                imagesViewModel.loadNextPage();
            }

                @Override
                public boolean isLastPage() {
                    boolean isLastPage = imagesViewModel.isLastPage();
                    if(isLastPage){
                        imageAdapter.setLoading(false);
                    }
                    return isLastPage;
                }

                @Override
                public boolean isLoading() {
                    return imagesViewModel.allPagesDownloaded.get();
                }
        });
    }

    @NonNull
    @Override
    protected String tag() {
        return "ImagesActivity";
    }

    @NonNull
    @Override
    protected ViewModelFactory<ImagesViewModel> getPresenterFactory() {
        return ()->new ImagesViewModel(this);
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull ImagesViewModel presenter) {
        imagesViewModel = presenter;
        initDataBinding();
        setSupportActionBar(activityImagesListBinding.toolbar);

        setUpListOfUsersView(activityImagesListBinding.imagesRecyclerView);

        presenter.onViewAttached(this);
        mDrawerLayout = activityImagesListBinding.drawerLayout;

        NavigationView navigationView = findViewById(R.id.nav_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);

                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()){
                            case R.id.nav_trends:
                                presenter.showTrending();
                                break;
                        }

                        return true;
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        activityImagesListBinding.searchView.setMenuItem(item);

        activityImagesListBinding.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                imagesViewModel.searchGIFs(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
