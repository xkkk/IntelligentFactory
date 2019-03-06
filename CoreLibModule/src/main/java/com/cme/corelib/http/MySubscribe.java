package com.cme.corelib.http;

import com.cme.corelib.CoreLib;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.NetworkUtils;
import com.cme.corelib.utils.UiUtil;

import rx.Subscriber;

/**
 * Created by Administrator on 2016-12-19
 *
 * @desc 封装的观察者
 */

public abstract class MySubscribe<T> extends Subscriber<T> {

    @Override
    public void onStart() {
        super.onStart();
        if (!NetworkUtils.isConnected(CoreLib.getContext())) {
            UiUtil.showToast("当前网络不可用，请检查网络情况");
            onCompleted();
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        UiUtil.showToast(e.getMessage());
        LogUtils.i("操作onError==" + e.getMessage());

    }

}
