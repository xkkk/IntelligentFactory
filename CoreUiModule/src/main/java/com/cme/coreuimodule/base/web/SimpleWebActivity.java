package com.cme.coreuimodule.base.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.cme.coreuimodule.base.activity.CommonBaseActivity;
import com.common.coreuimodule.R;

public class SimpleWebActivity extends CommonBaseActivity {
    public static final String FROM_LOAD_URL = "from_load_url";
    public static final String TITLE_NAME = "title_name";

    private String fromUrl = "";
    private String fromTitle = "";

    private SimpleWebFragment simpleWebFragment;

    protected int getLayoutId() {
        return R.layout.activity_simple_web;
    }

    public static void startActivity(Activity activity, String url, String name) {
        Intent intent = new Intent(activity, SimpleWebActivity.class);
        intent.putExtra(FROM_LOAD_URL, url);
        intent.putExtra(TITLE_NAME, name);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String url) {
        Intent intent = new Intent(activity, SimpleWebActivity.class);
        intent.putExtra(FROM_LOAD_URL, url);
        activity.startActivity(intent);
    }

    protected void initView() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initData() {
        Intent fromIntent = getIntent();
        if (fromIntent != null && fromIntent.hasExtra(FROM_LOAD_URL)) {
            fromUrl = fromIntent.getStringExtra(FROM_LOAD_URL);
        }
        if (getIntent().hasExtra(TITLE_NAME)) {
            fromTitle = getIntent().getStringExtra(TITLE_NAME);
        }
        if (TextUtils.isEmpty(fromUrl)) {
            finish();
        } else {
            simpleWebFragment = SimpleWebFragment.newInstance(fromUrl, fromTitle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, simpleWebFragment).commit();
            simpleWebFragment.setUserVisibleHint(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (simpleWebFragment != null) {
            if (simpleWebFragment.onBackPressed()) {
                return;
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
