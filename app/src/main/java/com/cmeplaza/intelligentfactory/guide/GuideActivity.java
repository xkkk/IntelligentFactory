package com.cmeplaza.intelligentfactory.guide;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.cme.corelib.utils.SharedPreferencesUtil;
import com.cme.coreuimodule.base.activity.CommonBaseActivity;
import com.cme.coreuimodule.base.widget.ScrollControlViewPager;
import com.cmeplaza.intelligentfactory.R;
import com.cmeplaza.intelligentfactory.guide.adapter.GuidePageAdapter;
import com.cmeplaza.intelligentfactory.login.LoginActivity;
import com.cmeplaza.intelligentfactory.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 引导页面
 */
public class GuideActivity extends CommonBaseActivity {
    @BindView(R.id.viewPager)
    ScrollControlViewPager viewPager;
    @BindView(R.id.tv_start)
    TextView tv_start;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initView() {
        List<Integer> imageRes = new ArrayList<>();
        imageRes.add(R.drawable.guide_image1);
        imageRes.add(R.drawable.guide_image2);
        imageRes.add(R.drawable.guide_image3);
        viewPager.setAdapter(new GuidePageAdapter(imageRes));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    tv_start.setVisibility(View.VISIBLE);
                } else {
                    tv_start.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.tv_start})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start:
                goMainPage();
                break;
        }
    }

    private void goMainPage() {
        SharedPreferencesUtil.getInstance().put(AppConstant.SpConstant.USER_FIRST_IN, false);
        commonStartActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        goMainPage();
    }
}
