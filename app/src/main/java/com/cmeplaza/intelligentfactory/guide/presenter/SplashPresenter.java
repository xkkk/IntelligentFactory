package com.cmeplaza.intelligentfactory.guide.presenter;

import com.cme.corelib.http.MySubscribe;
import com.cme.corelib.utils.SharedPreferencesUtil;
import com.cme.coreuimodule.base.mvp.RxPresenter;
import com.cmeplaza.intelligentfactory.MainActivity;
import com.cmeplaza.intelligentfactory.guide.contract.SplashContract;
import com.cmeplaza.intelligentfactory.utils.AppConstant;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by klx on 2018/5/10.
 * 启动页presenter
 */

public class SplashPresenter extends RxPresenter<SplashContract.IView> implements SplashContract.IPresenter {
    @Override
    public void goNextPage() {
        goNextPageWithDelay(1000);
    }

    @Override
    public void goNextPageWithDelay(long milliSecond) {
        final boolean isFirst = SharedPreferencesUtil.getInstance().get(AppConstant.SpConstant.USER_FIRST_IN, true);
        Observable.timer(milliSecond, TimeUnit.MILLISECONDS)
                .compose(mView.<Long>bind())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscribe<Long>() {
                    @Override
                    public void onNext(Long aLong) {
//                        if (isFirst) {
//                            mView.nextPage(GuideActivity.class, true);
//                        } else {
//                            mView.nextPage(MainActivity.class, true);
//                        }

                        mView.nextPage(MainActivity.class, true);
                    }
                });
    }
}
