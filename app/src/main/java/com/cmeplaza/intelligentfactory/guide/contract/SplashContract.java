package com.cmeplaza.intelligentfactory.guide.contract;

import com.cme.coreuimodule.base.mvp.BaseContract;

/**
 * Created by klx on 2018/5/10.
 *
 */

public interface SplashContract {
    interface IView extends BaseContract.BaseView{

    }

    interface IPresenter{
        void goNextPage();
        void goNextPageWithDelay(long milliSecond);
    }
}
