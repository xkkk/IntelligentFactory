package com.cme.corelib.utils.image;

import android.view.View;

import com.cme.corelib.image.ImageLoaderOptions;
import com.cme.corelibmodule.R;

/**
 * Created by klx on 2017/12/21.
 * 图片加载options
 */

public class BaseImageOptions {
    /**
     * 获取加载头像时的options
     *
     * @param view
     * @param url
     * @return
     */
    public static ImageLoaderOptions getPortraitOptions(View view, String url) {
        return getCommonOption(view, url, R.drawable.icon_chat_default_photo_square);
    }

    /**
     * 获取加载头像时的options
     *
     * @param view
     * @param url
     * @return
     */
    public static ImageLoaderOptions getCommonOption(View view, String url, int placeImage) {
        return getCommonOption(view, url, placeImage, placeImage);
    }

    /**
     * 获取加载头像时的options
     *
     * @param view
     * @param url
     * @return
     */
    public static ImageLoaderOptions getCommonOption(View view, String url, int placeImage, int errorImage) {
        return new ImageLoaderOptions.Builder(view, url)
                .placeholder(placeImage)
                .error(errorImage)
                .build();
    }

    /**
     * 获取加载头像时的options
     *
     * @param view
     * @param url
     * @return
     */
    public static ImageLoaderOptions getNoPlaceHolderOption(View view, String url) {
        return new ImageLoaderOptions.Builder(view, url)
                .build();
    }

    /**
     * 获取加载头像时的options
     *
     * @param view
     * @param resId
     * @return
     */
    public static ImageLoaderOptions getNoPlaceHolderOption(View view, Integer resId) {
        return new ImageLoaderOptions.Builder(view, resId)
                .build();
    }
}
