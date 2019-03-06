package com.cmeplaza.intelligentfactory.login.contract;

import com.cme.coreuimodule.base.mvp.BaseContract;

/**
 * Created by klx on 2018/5/11.
 */

public interface PerfectUserInfoContract {
    interface IView extends BaseContract.BaseView{
        void onEditSuccess();
    }

    interface IPresenter{
        /**
         * 完善个人信息
         * @param card 身份证号
         * @param invitationCode 邀请码
         * @param niceName 昵称
         * @param trueName 真实姓名
         */
        void editUserInfo(String card, String invitationCode, String niceName, String trueName);
    }
}
