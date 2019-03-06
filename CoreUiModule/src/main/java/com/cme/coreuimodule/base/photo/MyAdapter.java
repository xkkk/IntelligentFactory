package com.cme.coreuimodule.base.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cme.corelib.image.ImageLoaderManager;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.SizeUtils;
import com.cme.corelib.utils.StringUtils;
import com.cme.corelib.utils.UiUtil;
import com.cme.corelib.utils.image.BaseImageUtils;
import com.common.coreuimodule.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends CommonAdapter<String> {

    public List<String> mSelectedImage = new ArrayList<>();

    private String mDirPath;
    private int maxPic;

    public MyAdapter(Context context, List<String> mDatas,
                     String dirPath, int maxPic) {
        super(context, R.layout.ic_rong_de_ph_grid_item, mDatas);
        this.mDirPath = dirPath;
        this.maxPic = maxPic;
    }

    public ArrayList<String> getPicList() {
        if (mSelectedImage == null) {
            mSelectedImage = new ArrayList<String>();
        }
        Log.e("list", mSelectedImage.toString());
        return (ArrayList<String>) mSelectedImage;
    }

    public void setReset() {
        mSelectedImage.clear();
    }

    @Override
    protected void convert(ViewHolder helper, final String item, int position) {
        helper.setImageResource(R.id.id_item_image, R.drawable.ic_rong_pictures_no_white);
        helper.setImageResource(R.id.id_item_select,
                R.drawable.ic_rong_picture_unselected);
        ImageView id_item_image = helper.getView(R.id.id_item_image);

        ImageView iv_flag_image = helper.getView(R.id.iv_flag_image);
        TextView tv_video_length = helper.getView(R.id.tv_video_length);

        if (StringUtils.isVideo(item)) {  // 是否是视频
            iv_flag_image.setVisibility(View.VISIBLE);
            tv_video_length.setVisibility(View.VISIBLE);
            int size = SizeUtils.dp2px(mContext, 96);
            String videoPath = mDirPath + "/" + item;
            LogUtils.i("cme", "视频路径：" + videoPath);
            Bitmap bitmap = BaseImageUtils.getVideoThumbnail(videoPath, size, size, MediaStore.Images.Thumbnails.MICRO_KIND);
            id_item_image.setImageBitmap(bitmap);
        } else {
            LogUtils.i("cme", "图片路径：" + mDirPath + "/" + item);
            iv_flag_image.setVisibility(View.GONE);
            tv_video_length.setVisibility(View.GONE);
            ImageLoaderManager.getInstance().showSimpleImage(id_item_image, "file:///" + mDirPath + "/" + item);
        }

        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);

        mImageView.setColorFilter(null);
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedImage.contains(mDirPath + "/" + item)) {
                    mSelectedImage.remove(mDirPath + "/" + item);
                    mSelect.setImageResource(R.drawable.ic_rong_picture_unselected);
                    mImageView.setColorFilter(null);
                } else {
                    if (mSelectedImage.size() < maxPic) {
                        mSelectedImage.add(mDirPath + "/" + item);
                        mSelect.setImageResource(R.drawable.ic_rong_pictures_selected);
                        mImageView.setColorFilter(Color.parseColor("#77000000"));
                    } else {
                        UiUtil.showToast(mContext, "最多显示" + maxPic + "张");
                    }
                }
                Log.i("size", "count-->" + mSelectedImage.size());

            }
        });

        if (mSelectedImage.contains(mDirPath + "/" + item)) {
            mSelect.setImageResource(R.drawable.ic_rong_pictures_selected);
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        }
    }
}
