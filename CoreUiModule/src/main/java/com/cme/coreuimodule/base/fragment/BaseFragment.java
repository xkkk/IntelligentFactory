package com.cme.coreuimodule.base.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cme.corelib.event.UIEvent;
import com.cme.coreuimodule.base.activity.CommonBaseActivity;
import com.cme.coreuimodule.base.utils.CommonDialogUtils;
import com.cme.coreuimodule.base.widget.MyLoadMoreWrapper;
import com.common.coreuimodule.R;
import com.trello.rxlifecycle.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：Android_AJ on 2017/4/6.
 * 邮箱：ai15116811712@163.com
 * 版本：v1.0
 */
public abstract class BaseFragment extends RxFragment implements
        MyLoadMoreWrapper.OnLoadMoreListener {
    protected View rootView;
    private Unbinder unbinder;
    // 下拉刷新
    protected SwipeRefreshLayout swipe_refresh;
    private boolean isRefresh = false;

    // 加载更多
    protected MyLoadMoreWrapper loadMoreWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), null);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        initView();
        initBack();
        initRefreshLayout();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    protected void initRefreshLayout() {
        swipe_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
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
                onLayoutRefresh();
            }
        });
    }

    protected void initLoadMoreWrapper(RecyclerView.Adapter commonAdapter) {
        loadMoreWrapper = new MyLoadMoreWrapper(commonAdapter);
        loadMoreWrapper.setLoadMoreView(R.layout.layout_load_more_recycler);
        loadMoreWrapper.setOnLoadMoreListener(this);
    }

    protected void hasMore(boolean flag, List mData) {
        loadMoreWrapper.setLoadOver(flag);
        if (!flag) {
            if (mData != null && mData.size() >= 1) {
                loadMoreWrapper.notifyItemRangeRemoved(mData.size(), mData.size() + 1);
            } else {
                loadMoreWrapper.notifyItemRangeRemoved(0, 1);
            }
        }
    }

    /**
     * @param flag        能否加载
     * @param mData       集合数据
     * @param isHasHeader 是否有头部布局
     */
    protected void hasMore(boolean flag, List mData, boolean isHasHeader) {
        loadMoreWrapper.setLoadOver(flag);
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

    @Override
    public void onLoadMoreRequested() {

    }

    /**
     * 下拉刷新后调用的方法
     */
    protected void onLayoutRefresh() {

    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void initData() {
    }

    protected void commonStartActivity(Class<? extends CommonBaseActivity> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        commonStartActivity(intent);
    }

    protected void commonStartActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private void initBack() {
        TextView tv_back = ButterKnife.findById(rootView, R.id.tv_back);
        if (tv_back != null) {
            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
    }

    protected void showBack() {
        TextView tv_back = ButterKnife.findById(rootView, R.id.tv_back);
        if (tv_back != null) {
            visible(tv_back);
        }
    }

    protected void hideBack() {
        TextView tv_back = ButterKnife.findById(rootView, R.id.tv_back);
        if (tv_back != null) {
            gone(tv_back);
        }
    }

    protected void setTitleCenter(int res) {
        setTitleCenter(getActivity().getResources().getString(res));
    }

    protected void setTitleCenter(String title) {
        if (rootView == null) {
            return;
        }
        TextView tv_title_center = (TextView) rootView.findViewById(R.id.tv_title_center);
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

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
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

    /**
     * 默认加载框
     */
    public void showProgress() {
        showProgress(getString(R.string.loading));
    }

    /**
     * 默认加载框
     */
    protected void showProgress(int id) {
        String message = getString(id);
        showProgress(message);
    }

    private AlertDialog progressDialog;

    /**
     * 显示加载框
     *
     * @param message
     */
    protected void showProgress(String message) {
        progressDialog = CommonDialogUtils.getProgressDialog(getActivity(), message);
        progressDialog.show();
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

    protected void showOnlyConfirmDialog(String message) {
        CommonDialogUtils.showOnlyConfirmDialog(getActivity(), message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUIEvent(UIEvent uiEvent) {

    }
}
