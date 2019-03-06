package com.cme.corelib.image.glideloader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.cme.corelib.image.BitmapUtils;
import com.cme.corelib.image.IImageLoaderstrategy;
import com.cme.corelib.image.ImageLoaderOptions;
import com.cme.corelib.image.listener.ImageLoaderListener;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelibmodule.R;

/**
 * Created by klx on 2017/8/12.
 * glide 模式
 */

public class GlideImageLoader implements IImageLoaderstrategy {
    private static final String TAG = "GlideImageLoader";
    private Handler mainHandler = new Handler();

    @Override
    public void showImage(ImageLoaderOptions options) {
        GenericRequestBuilder mGenericRequestBuilder = init(options);
        if (mGenericRequestBuilder != null) {
            showImageLast(mGenericRequestBuilder, options, null);
        }
    }

    @Override
    public void showImage(@NonNull ImageLoaderOptions options, ImageLoaderListener imageLoaderListener) {
        GenericRequestBuilder mGenericRequestBuilder = init(options);
        if (mGenericRequestBuilder != null) {
            showImageLast(mGenericRequestBuilder, options, imageLoaderListener);
        }
    }

    @Override
    public void cleanMemory(Context context) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Glide.get(context).clearMemory();
        }
    }

    @Override
    public void init(Context context) {
        ViewTarget.setTagId(R.id.tag_glide);
    }

    @Override
    public void hideImage(@NonNull View view, int isVisiable) {
        view.setVisibility(isVisiable);
    }

    @Override
    public void pause(Context context) {
        Glide.with(context).pauseRequests();
    }

    @Override
    public void resume(Context context) {
        Glide.with(context).resumeRequests();
    }

    public DrawableTypeRequest getGenericRequestBuilder(RequestManager manager, ImageLoaderOptions options) {

        if (!TextUtils.isEmpty(options.getUrl())) {
            return manager.load(options.getUrl());
        }
        return manager.load(options.getResource());
    }

    public RequestManager getRequestManager(Context context) {
        return Glide.with(context);

    }

    public GenericRequestBuilder init(ImageLoaderOptions options) {
        View v = options.getViewContainer();

        RequestManager manager = getRequestManager(v.getContext());
        if (v instanceof ImageView) {
            GenericRequestBuilder mDrawableTypeRequest = getGenericRequestBuilder(manager, options).asBitmap();
            if (options.isAsGif()) {
                mDrawableTypeRequest = getGenericRequestBuilder(manager, options);
            }
            //装载参数
            mDrawableTypeRequest = loadGenericParams(mDrawableTypeRequest, options);
            return mDrawableTypeRequest;
        }
        return null;
    }


    private GenericRequestBuilder loadGenericParams(GenericRequestBuilder mGenericRequestBuilder, final ImageLoaderOptions options) {
        final View view = options.getViewContainer();
        GenericRequestBuilder builder = mGenericRequestBuilder;
        if (mGenericRequestBuilder instanceof DrawableTypeRequest) {
            if (options.isCrossFade()) {
                ((DrawableTypeRequest) mGenericRequestBuilder).crossFade();
            }
            if (options.isAsGif()) {
                builder = ((DrawableTypeRequest) mGenericRequestBuilder).asGif();
            }
        }
        builder.skipMemoryCache(options.isSkipMemoryCache());
        if (options.getImageSize() != null) {
            int width = getSize(options.getImageSize().getWidth(), view);
            int height = getSize(options.getImageSize().getHeight(), view);
            Log.i(TAG, "load params " + options.getImageSize().getWidth() + "  : " + options.getImageSize().getHeight());
            builder.override(width, height);
        }
        if (options.getHolderDrawable() != -1) {
            builder.placeholder(options.getHolderDrawable());
        }
        if (options.getErrorDrawable() != -1) {
            builder.error(options.getErrorDrawable());
        }

        if (options.getDiskCacheStrategy() != ImageLoaderOptions.DiskCacheStrategy.DEFAULT) {
            switch (options.getDiskCacheStrategy()) {
                case NONE:
                    builder.diskCacheStrategy(DiskCacheStrategy.NONE);
                    break;
                case All:
                    builder.diskCacheStrategy(DiskCacheStrategy.ALL);
                    break;
                case SOURCE:
                    builder.diskCacheStrategy(DiskCacheStrategy.SOURCE);
                    break;
                case RESULT:
                    builder.diskCacheStrategy(DiskCacheStrategy.RESULT);
                    break;
                default:
                    builder.diskCacheStrategy(DiskCacheStrategy.ALL);
                    break;
            }
        }


        return builder;

    }

    private void showImageLast(GenericRequestBuilder mDrawableTypeRequest, final ImageLoaderOptions options, final ImageLoaderListener imageLoaderListener) {
        final ImageView img = (ImageView) options.getViewContainer();
        mDrawableTypeRequest.thumbnail(0.1f);
        // 是否使用高斯模糊
        if (options.isBlurImage()) {
            // 具体的高斯模糊这里就不实现了，直接展示图片
            mDrawableTypeRequest.into(new SimpleTarget<Bitmap>() {

                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    LogUtils.i(TAG, "开始加载图片");
                }

                @Override
                public void onStop() {
                    super.onStop();
                    LogUtils.i(TAG, "结束加载图片");
                }

                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    if (resource != null && img != null) {
                        try {
                            final Bitmap result = BitmapUtils.bitmapSetSize(resource, options.getImageSize().getWidth(), options.getImageSize().getHeight());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (result != null && img != null) {
                                        img.setImageBitmap(result);
                                    }
                                }
                            });
                        } catch (OutOfMemoryError e) {
                            if (img != null && resource != null) {
                                img.setImageBitmap(resource);
                            }
                        }
                    } else {
                        Log.e("imageloader", "resource null");
                    }
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    Log.e("iamgeloader", "resource load failed");
                    if (e != null) e.printStackTrace();
                }
            });
            return;
        }
        // 是否展示一个gif
        if (options.isAsGif()) {
            ((GifRequestBuilder) mDrawableTypeRequest).dontAnimate().into(new SimpleTarget<GifDrawable>() {
                @Override
                public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
                    img.setImageDrawable(resource);
                    resource.start();
                }
            });
            return;
        }
        if (options.getTarget() != null) {
            mDrawableTypeRequest.into(options.getTarget());
            return;
        }
//        mDrawableTypeRequest.into(img);
        mDrawableTypeRequest.into(new BitmapImageViewTarget(img) {

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                if (imageLoaderListener != null) {
                    imageLoaderListener.onLoadStart();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                super.onResourceReady(resource, glideAnimation);
                if (imageLoaderListener != null) {
                    imageLoaderListener.onLoadFinish();
                }
            }

            @Override
            public void onStop() {
                super.onStop();
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                if (imageLoaderListener != null) {
                    imageLoaderListener.onLoadFinish();
                }
            }
        });
    }

    /**
     * 获取资源尺寸
     *
     * @param resSize
     * @return 默认返回原始尺寸
     */
    private int getSize(int resSize, View container) {
        if (resSize <= 0) {
            return SimpleTarget.SIZE_ORIGINAL;
        } else {
            try {
                return container.getContext().getResources().getDimensionPixelOffset(resSize);

            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                Log.e("", "I got !!!");
                return resSize;
            }
        }
    }

}
