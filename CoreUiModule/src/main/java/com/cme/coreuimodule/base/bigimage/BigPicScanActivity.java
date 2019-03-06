package com.cme.coreuimodule.base.bigimage;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.cme.corelib.secret.Sha1;
import com.cme.corelib.utils.UiUtil;
import com.cme.coreuimodule.base.activity.MyBaseRxActivity;
import com.cme.coreuimodule.base.widget.CircleIndicator;
import com.cme.coreuimodule.base.widget.ScrollControlViewPager;
import com.common.coreuimodule.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 大图浏览界面
 */
public class BigPicScanActivity extends MyBaseRxActivity<DownLoadFilePresenter> implements DownLoadFileContract.IDownLoadFileView {

    public static final String FROM_IMAGES = "from_images";
    public static final String IMAGE_INDEX = "image_index";

    private ScrollControlViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ImageView iv_all_pic;
    private ImageView iv_downLoad;

    private List<String> images;
    private ViewPagerImageAdapter adapter;
    private int page = 0;//图片索引
    private boolean isDownLoading = false;


    public static void startActivity(Activity activity, View view, ArrayList<String> images) {
        Intent imageIntent = new Intent(activity, BigPicScanActivity.class);
        imageIntent.putStringArrayListExtra(BigPicScanActivity.FROM_IMAGES, images);
        if (Build.VERSION.SDK_INT >= 21) {
            activity.startActivity(imageIntent, ActivityOptions.makeSceneTransitionAnimation(activity, view, "shareTransition").toBundle());
        } else {
            activity.startActivity(imageIntent);
            activity.overridePendingTransition(R.anim.zoomin, 0);
        }
    }

    @Override
    protected void beforeSetContentView() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_big_pic_scan;
    }

    @Override
    protected void initView() {
        viewPager = (ScrollControlViewPager) findViewById(R.id.viewPager);
        circleIndicator = (CircleIndicator) findViewById(R.id.circleIndicator);
        iv_all_pic = (ImageView) findViewById(R.id.iv_all_pic);
        iv_downLoad = (ImageView) findViewById(R.id.iv_downLoad);

        gone(iv_all_pic, iv_downLoad);
        initViewPager();
    }

    private void initViewPager() {
        if (getIntent().hasExtra(FROM_IMAGES)) {
            images = getIntent().getStringArrayListExtra(FROM_IMAGES);
        }
        if (getIntent().hasExtra(IMAGE_INDEX)) {
            page = getIntent().getIntExtra(IMAGE_INDEX, 0);
        }
        if (images == null || images.size() == 0) {
            UiUtil.showToast("数据异常");
            finish();
        }
        adapter = new ViewPagerImageAdapter(this, images);
        adapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
        viewPager.setCurrentItem(page);//设置传过来的索引
        adapter.setOnImageClickListener(new ViewPagerImageAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(View view, int position) {
                if (Build.VERSION.SDK_INT >= 21) {
                    onBackPressed();
                } else {
                    onBackPressed();
                    overridePendingTransition(0, R.anim.zoomout);
                }
            }

            @Override
            public void onShowAllClick(int position, View view) {

            }

            @Override
            public void onDownLoadIvClick(String url) {
                if (isDownLoading) {
                    return;
                }
                mPresenter.downLoadImage(url, Sha1.shaEncrypt(url).substring(0, 10) + ".jpg");
                isDownLoading = true;
            }

            @Override
            public void onLongClick(int position, View view) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                isDownLoading = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onDownLoadResult(boolean result, String path) {
        isDownLoading = false;
        if (result) {
            Toast.makeText(this, "图片已保存至" + path, Toast.LENGTH_SHORT).show();
        } else {
            UiUtil.showToast("下载失败，请重试");
        }
    }

    @Override
    public void downLoadProgress(float progress, long total) {

    }

    @Override
    protected DownLoadFilePresenter createPresenter() {
        return new DownLoadFilePresenter();
    }
}
