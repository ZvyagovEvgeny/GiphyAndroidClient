package com.universetelecom.mvvm_users.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.universetelecom.mvvm_users.R;

import com.universetelecom.mvvm_users.databinding.ItemImageBinding;

import com.universetelecom.mvvm_users.model.giphy.Datum;
import com.universetelecom.mvvm_users.viewModel.ItemImageViewModel;

import java.util.ArrayList;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Datum> imagesList;

    private final int LOADING = 1;
    private final int ITEM = 2;

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
    private boolean isLoading = true;
    public ImageAdapter() {
        this.imagesList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType==ITEM){
            ItemImageBinding itemImageBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_image ,parent, false);

            return new ImageAdapterViewHolder(itemImageBinding);
        }else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_progress_bar, parent, false);
            return new ViewHolderLoading(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageAdapterViewHolder) {
            Datum datum = imagesList.get(position);

            ImageAdapterViewHolder imageAdapterViewHolder = (ImageAdapterViewHolder) holder;
            imageAdapterViewHolder.bindImage(imagesList.get(position));

        } else if (holder instanceof ViewHolderLoading) {
            ViewHolderLoading loadingViewHolder = (ViewHolderLoading) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        if(imagesList.size()==0 || !isLoading) return imagesList.size();
        return imagesList.size() + 1; // to show progress bar
    }
    public void updateData(@Nullable List<Datum> data) {
        this.imagesList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==imagesList.size())
            return LOADING;
        else
            return ITEM;
    }

    public static class ImageAdapterViewHolder extends RecyclerView.ViewHolder {

        ItemImageBinding itemImageBinding;

        public ImageAdapterViewHolder(ItemImageBinding itemImageBinding) {
            super(itemImageBinding.imageCardView);
            this.itemImageBinding = itemImageBinding;
        }

        void bindImage(Datum image){
            if(itemImageBinding.getImageViewModel() == null){
                itemImageBinding.setImageViewModel(new ItemImageViewModel(image, itemView.getContext()));
            }else {
                itemImageBinding.getImageViewModel().setImage(image);
            }
        }
    }

    private class ViewHolderLoading extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ViewHolderLoading(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.itemProgressbar);
        }
    }
}
