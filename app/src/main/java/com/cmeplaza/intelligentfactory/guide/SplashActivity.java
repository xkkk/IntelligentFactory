package com.cmeplaza.intelligentfactory.guide;

import com.cme.coreuimodule.base.activity.MyBaseRxActivity;
import com.cmeplaza.intelligentfactory.R;
import com.cmeplaza.intelligentfactory.guide.contract.SplashContract;
import com.cmeplaza.intelligentfactory.guide.presenter.SplashPresenter;

import butterknife.BindView;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 启动页面
 */
public class SplashActivity extends MyBaseRxActivity<SplashPresenter> implements SplashContract.IView {
    @BindView(R.id.iv_splash)
    GifImageView iv_splash;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        GifDrawable gifDrawable = (GifDrawable) iv_splash.getDrawable();
        if (gifDrawable != null) {
            gifDrawable.setLoopCount(1);
            gifDrawable.addAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationCompleted(int loopNumber) {
                    mPresenter.goNextPageWithDelay(0);
                }
            });
        } else {
            mPresenter.goNextPage();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter();
    }

}
