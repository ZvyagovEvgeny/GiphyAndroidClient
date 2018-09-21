package com.universetelecom.mvvm_users.view.adapter;

import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;

import com.universetelecom.mvvm_users.model.giphy.Datum;

import java.util.List;

public class RecyclerViewDataBinding  {

    @BindingAdapter("data")
    public static void bind(RecyclerView recyclerView, List<Datum> data) {
        if(recyclerView.getAdapter() instanceof ImageAdapter){
            ((ImageAdapter) recyclerView.getAdapter()).updateData(data);
        }
    }

    @BindingAdapter({"allPagesDownloaded"})
    public static void bind(RecyclerView recyclerView, ObservableBoolean allPagesDownloaded) {
        if(recyclerView.getAdapter() instanceof ImageAdapter){
            ((ImageAdapter) recyclerView.getAdapter()).setLoading(!allPagesDownloaded.get());
        }
    }

}
