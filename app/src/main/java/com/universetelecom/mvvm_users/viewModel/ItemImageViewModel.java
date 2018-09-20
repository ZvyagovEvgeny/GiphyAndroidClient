package com.universetelecom.mvvm_users.viewModel;

import android.content.Context;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;

import android.util.Log;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.universetelecom.mvvm_users.R;

import com.universetelecom.mvvm_users.model.giphy.Datum;



public class ItemImageViewModel extends BaseObservable {

    private Datum image;
    private Context context;


    public ItemImageViewModel(Datum image, Context context){
        this.image = image;
        this.context = context;
    //    imageWidth.set(image.getImages().getFixedHeightDownsampled().getWidth());
     //   imageHeight.set(image.getImages().getFixedHeightDownsampled().getHeight());


    }


    @BindingAdapter("imageUrlGif")
    public static void setImageUrl(ImageView imageView, String url){
        Log.d("image", "Start: "+url);

        Glide.with(imageView.getContext()).load(url)
                .thumbnail(Glide.with(imageView.getContext()).load(R.drawable.load_progress_bar))
                .fitCenter()
                .crossFade()
                .into(imageView);
    }

    public String getImageUrl() {
        return image.getImages().getFixedHeightDownsampled().getUrl();
    }

    public String getUserName(){
        return "someName";
    }

    public void setImage(Datum image) {
        this.image = image;
        notifyChange();
    }

}
