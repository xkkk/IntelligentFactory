package com.cmeplaza.intelligentfactory.login.contract;

import com.cme.coreuimodule.base.mvp.BaseContract;

import java.io.InputStream;

/**
 * Created by klx on 2018/5/10.
 */

public interface LoginContract {
    interface IView extends BaseContract.BaseView {
        /**
         * 发送短信验证码结果
         */
        void onSendSMSVerifyCodeResult(boolean result);

        /**
         * 登录结果
         */
        void onLoginResult(boolean result,boolean isNeedEditUserInfo);

        void onGetPicVerifyResult(byte[] inputStream);
    }

    interface IPresenter {
        /**
         * 登录
         *
         * @param username           手机号
         * @param verifyCode         短信验证码
         * @param numberVerifyNumber 数字验证码
         */
        void login(String username, String verifyCode, String numberVerifyNumber);

        /**
         * 获取短信验证码
         *
         * @param phoneNumber 手机号
         */
        void getSMSVerifyCode(String phoneNumber);

        /**
         * 获取数字验证码字符流地址
         */
        void getImageVerifyCode();
    }
}
