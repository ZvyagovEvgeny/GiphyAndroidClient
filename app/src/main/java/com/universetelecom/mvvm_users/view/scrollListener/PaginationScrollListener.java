package com.universetelecom.mvvm_users.view.scrollListener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class PaginationScrollListener extends RecyclerView.OnScrollListener  {

    LinearLayoutManager layoutManager;

    public interface LoadMoreItems{
        void loadMoreItems();
    }
    private LoadMoreItems loadMoreItemsCallback;

    public PaginationScrollListener(LinearLayoutManager layoutManager, LoadMoreItems loadMoreItems) {
        this.layoutManager = layoutManager;
        this.loadMoreItemsCallback = loadMoreItems;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0) {
            loadMoreItemsCallback.loadMoreItems();
        }
    }
}
