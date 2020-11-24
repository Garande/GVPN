package com.garande.g_vpn.utils;

import android.net.Uri;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.garande.g_vpn.BuildConfig;

import de.hdodenhof.circleimageview.CircleImageView;

public class Utils {
    public static String getDrawablePath(int drawable){
        Uri uri = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/"+drawable);
        return "android.resource://"+ BuildConfig.APPLICATION_ID+"/"+drawable;
    }


    @BindingAdapter("imageSrc")
    public static void loadImage(CircleImageView view, String imageUrl){
        Glide.with(view.getContext()).load(imageUrl).into(view);
//                .apply(new RequestOptions().circleCrop())
//                .into(view);
    }

    @BindingAdapter("imageResource")
    public static void setImageResource(CircleImageView imageView, int resource){
        imageView.setImageResource(resource);
    }
}
