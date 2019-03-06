package com.cmeplaza.intelligentfactory.guide.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cme.corelib.utils.image.BaseImageUtils;
import com.cmeplaza.intelligentfactory.R;

import java.util.List;

/**
 * Created by klx on 2018/5/10.
 * 引导页面的adapter
 */

public class GuidePageAdapter extends PagerAdapter {
    private List<Integer> imageRes;

    public GuidePageAdapter(List<Integer> imageRes) {
        this.imageRes = imageRes;
    }

    @Override
    public int getCount() {
        return imageRes.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View rootView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_image_only, null);
        ImageView iv_image = (ImageView) rootView.findViewById(R.id.iv_image);
        BaseImageUtils.showImage(iv_image, imageRes.get(position));
        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
