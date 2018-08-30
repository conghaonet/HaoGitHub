package com.app2m.github.hub;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;

public class TryGlide {
    public void setBackground(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .into(new MyCustomViewTarget(view));
    }

    class MyCustomViewTarget extends CustomViewTarget<ImageView, Drawable> {

        /**
         * Constructor that defaults {@code waitForLayout} to {@code false}.
         *
         * @param view
         */
        public MyCustomViewTarget(@NonNull ImageView view) {
            super(view);
        }

        @Override
        protected void onResourceCleared(@Nullable Drawable placeholder) {

        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {

        }

        @Override
        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

        }
    }
}
