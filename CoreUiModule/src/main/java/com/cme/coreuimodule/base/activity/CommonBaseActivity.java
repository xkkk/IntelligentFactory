package com.cme.coreuimodule.base.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cme.corelib.CoreLib;
import com.cme.corelib.event.UIEvent;
import com.cme.corelib.utils.NetworkUtils;
import com.cme.corelib.utils.SharedPreferencesUtil;
import com.cme.corelib.utils.SizeUtils;
import com.cme.corelib.utils.UiUtil;
import com.cme.coreuimodule.base.mvp.BaseContract;
import com.cme.coreuimodule.base.utils.CommonDialogUtils;
import com.cme.coreuimodule.base.widget.DividerDecoration;
import com.cme.coreuimodule.base.widget.MyLoadMoreWrapper;
import com.common.coreuimodule.R;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by klx on 2017/9/5.
 * 通用CommonBaseActivity
 */

public abstract class CommonBaseActivity extends RxAppCompatActivity implements MyLoadMoreWrapper.OnLoadMoreListener, BaseContract.BaseView {
    public static String APP_TEXT_SIZE = "app_text_size";
    // 下拉刷新
    protected SwipeRefreshLayout swipe_refresh;
    protected boolean canLoadMore = false;
    // 加载更多
    protected MyLoadMoreWrapper loadMoreWrapper;
    private Unbinder unbinder;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        CoreLib.activityList.add(this);
        EventBus.getDefault().register(this);
        beforeSetContentView();
        setTextTheme();
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        initBack();
        initRefreshLayout();
        initView();
        initData();

    }

    protected void beforeSetContentView() {
    }

    /**
     * 设置字体大小属性
     */
    private void setTextTheme() {
        int textSize = SharedPreferencesUtil.getInstance().get(APP_TEXT_SIZE, 1);
        switch (textSize) {
            case 0:
                setTheme(R.style.Theme_Text_0);
                break;
            case 1:
                setTheme(R.style.Theme_Text_1);
                break;
            case 2:
                setTheme(R.style.Theme_Text_2);
                break;
            case 3:
                setTheme(R.style.Theme_Text_3);
                break;
            case 4:
                setTheme(R.style.Theme_Text_4);
                break;
            case 5:
                setTheme(R.style.Theme_Text_5);
                break;
        }
    }

    protected abstract int getLayoutId();

    private void initBack() {
        TextView tv_back = ButterKnife.findById(this, R.id.tv_back);
        if (tv_back != null) {
            if (tv_back.getVisibility() == View.VISIBLE) {
                tv_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }

    protected void initRefreshLayout() {
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        if (swipe_refresh == null) {
            return;
        }
        swipe_refresh.setColorSchemeResources(R.color.pink_dark, R.color.pink_light,
                R.color.colorAccentDark);
        swipe_refresh.setProgressViewOffset(false, -100, getResources().getDisplayMetrics().heightPixels / 10);
        swipe_refresh.setRefreshing(false);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isAvailable(getApplicationContext())) {
                    onLayoutRefresh();
                } else {
                    UiUtil.showToast(R.string.net_error);
                    swipe_refresh.setRefreshing(false);
                }
            }
        });
    }

    protected abstract void initView();

    protected void initData() {
    }

    /**
     * 下拉刷新后调用的方法
     */
    protected void onLayoutRefresh() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        CoreLib.isResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        CoreLib.isResume = false;
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        CoreLib.activityList.remove(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onLoadMoreRequested() {

    }

    protected void initLoadMoreWrapper(RecyclerView.Adapter commonAdapter) {
        loadMoreWrapper = new MyLoadMoreWrapper(commonAdapter);
        loadMoreWrapper.setLoadMoreView(R.layout.layout_load_more_recycler);
        loadMoreWrapper.setOnLoadMoreListener(this);
    }

    /**
     * @param flag  能否加载
     * @param mData 集合数据
     */
    protected void hasMore(boolean flag, List mData) {
        loadMoreWrapper.setLoadOver(flag);
        canLoadMore = flag;
        if (!flag) {
            if (mData != null) {
                if (loadMoreWrapper.getItemCount() == mData.size()) {
                    return;
                }
                loadMoreWrapper.notifyItemRangeRemoved(mData.size(), mData.size() + 1);
            } else {
                loadMoreWrapper.notifyItemRangeRemoved(0, 1);
            }
        } else {
            loadMoreWrapper.setLoadMoreView(R.layout.layout_load_more_recycler);
        }
    }

    /**
     * @param flag        能否加载
     * @param mData       集合数据
     * @param isHasHeader 是否有头部布局
     */
    protected void hasMore(boolean flag, List mData, boolean isHasHeader) {
        loadMoreWrapper.setLoadOver(flag);
        canLoadMore = flag;
        if (!flag) {
            if (isHasHeader) {//如果有头部 position +1
                if (mData != null) {
                    if (loadMoreWrapper.getItemCount() == mData.size()) {
                        return;
                    }
                    loadMoreWrapper.notifyItemRangeRemoved(mData.size() + 1, mData.size() + 2);
                } else {
                    loadMoreWrapper.notifyItemRangeRemoved(1, 2);
                }
            } else {
                if (mData != null) {
                    if (loadMoreWrapper.getItemCount() == mData.size()) {
                        return;
                    }
                    loadMoreWrapper.notifyItemRangeRemoved(mData.size(), mData.size() + 1);
                } else {
                    loadMoreWrapper.notifyItemRangeRemoved(0, 1);
                }
            }

        } else {
            loadMoreWrapper.setLoadMoreView(R.layout.layout_load_more_recycler);
        }
    }

    /**
     * 默认加载框
     */
    protected void showProgress(int id) {
        String message = getString(id);
        showProgress(message);
    }

    /**
     * 显示加载框
     */
    protected void showProgress(String message) {
        progressDialog = CommonDialogUtils.getProgressDialog(this, message);
        progressDialog.show();
    }

    protected void showBack() {
        TextView tv_back = ButterKnife.findById(this, R.id.tv_back);
        if (tv_back != null) {
            visible(tv_back);
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    protected void setTitleCenter(int res) {
        setTitleCenter(getString(res));
    }

    protected void setTitleCenter(String title) {
        TextView tv_title_center = ButterKnife.findById(this, R.id.tv_title_center);
        if (tv_title_center != null) {
            tv_title_center.setText(title);
        }
    }

    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void inVisible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void showError(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        UiUtil.showToast(message);
    }

    /**
     * 默认加载框
     */
    public void showProgress() {
        showProgress(getString(R.string.loading));
    }

    /**
     * 隐藏加载框
     */
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (swipe_refresh != null) {
            swipe_refresh.setRefreshing(false);
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bind() {
        return bindToLifecycle();
    }

    @Override
    public void nextPage(Class clazz, boolean isFinishThisPage) {
        commonStartActivity(new Intent(this, clazz));
        if (isFinishThisPage) {
            finish();
        }
    }

    @Override
    public void nextPageWithSingleActivity(Class clazz) {
        Intent loginIntent = new Intent(this, clazz);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    protected void commonStartActivity(Intent intent) {
        startActivity(intent);
    }

    protected void showOnlyConfirmDialog(String message) {
        CommonDialogUtils.showOnlyConfirmDialog(this, message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initCommonRecyclerView(RecyclerView recyclerView) {
        DividerDecoration dividerDecoration = new DividerDecoration(CoreLib.getContext(),
                CoreLib.getContext().getResources().getColor(R.color.global_split_line_color),
                SizeUtils.dp2px(CoreLib.getContext(), 0.5f));
        dividerDecoration.setLinePadding(10);
        recyclerView.addItemDecoration(dividerDecoration);
        recyclerView.setHasFixedSize(true);
    }

    protected boolean isCompatible(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUiEvent(UIEvent uiEvent) {

    }
}
