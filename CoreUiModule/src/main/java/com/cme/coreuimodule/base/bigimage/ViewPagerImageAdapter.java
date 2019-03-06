package com.cme.coreuimodule.base.bigimage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.cme.corelib.image.ImageLoaderManager;
import com.cme.corelib.image.listener.ImageLoaderListener;
import com.cme.corelib.utils.image.BaseImageOptions;
import com.cme.corelib.utils.image.BaseImageUtils;
import com.common.coreuimodule.R;

import java.util.List;

/**
 * Created by klx on 2017/6/24.
 * 大图浏览页面
 */

public class ViewPagerImageAdapter extends PagerAdapter {
    private List<String> mData;
    private Activity activity;

    public ViewPagerImageAdapter(Activity activity, List<String> mData) {
        this.activity = activity;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View rootView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_image_scan, null);
        final PhotoView photoView = (PhotoView) rootView.findViewById(R.id.iv_image);
        final LinearLayout ll_loading = (LinearLayout) rootView.findViewById(R.id.ll_loading);
        ImageView iv_downLoad = (ImageView) rootView.findViewById(R.id.iv_downLoad);

        photoView.enable();
        photoView.setMaxScale(3);
        // 加载图片
        String picUrl = mData.get(position);
        if (!TextUtils.isEmpty(picUrl)) {
            if (!picUrl.startsWith("http") && BaseImageUtils.isLocalFileExist(picUrl)) {
                Bitmap bitmap = BaseImageUtils.getLocalBitmap(picUrl);
                photoView.setImageBitmap(bitmap);
            } else {
                ImageLoaderManager.getInstance()
                        .showImage(BaseImageOptions.getCommonOption(photoView, picUrl, R.drawable.bg_loading_image_gray,
                                R.drawable.chat_image_loading_fail_big), new ImageLoaderListener() {
                            @Override
                            public void onLoadStart() {
                                ll_loading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadFinish() {
                                ll_loading.setVisibility(View.GONE);
                            }
                        });
            }
        }
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImageClickListener != null) {
                    onImageClickListener.onImageClick(photoView, position);
                }
            }
        });
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onImageClickListener != null) {
                    onImageClickListener.onLongClick(position, photoView);
                }
                return true;
            }
        });
        iv_downLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImageClickListener != null) {
                    String picUrl = mData.get(position);
                    if (!picUrl.startsWith("http") && BaseImageUtils.isLocalFileExist(picUrl)) {
                        Toast.makeText(activity, "图片已保存至" + picUrl, Toast.LENGTH_SHORT).show();
                    } else {
                        onImageClickListener.onDownLoadIvClick(mData.get(position));
                    }

                }
            }
        });
        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    private OnImageClickListener onImageClickListener;

    public interface OnImageClickListener {
        void onImageClick(View view, int position);

        void onShowAllClick(int position, View view);

        void onDownLoadIvClick(String url);

        void onLongClick(int position, View view);
    }
}
