package com.cmeplaza.intelligentfactory.login.presenter;

import android.text.TextUtils;

import com.cme.corelib.CoreLib;
import com.cme.corelib.bean.BaseModule;
import com.cme.corelib.http.MySubscribe;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.RegularUtils;
import com.cme.corelib.utils.SharedPreferencesUtil;
import com.cme.corelib.utils.UiUtil;
import com.cme.coreuimodule.base.mvp.RxPresenter;
import com.cmeplaza.intelligentfactory.R;
import com.cmeplaza.intelligentfactory.login.bean.PersonalInfoBean;
import com.cmeplaza.intelligentfactory.login.contract.LoginContract;
import com.cmeplaza.intelligentfactory.utils.AppConstant;
import com.cmeplaza.intelligentfactory.utils.HttpUtils;
import com.cmeplaza.intelligentfactory.utils.UserInfoUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by klx on 2018/5/10.
 * 登录页面presenter
 */

public class LoginPresenter extends RxPresenter<LoginContract.IView> implements LoginContract.IPresenter {
    @Override
    public void login(final String username, String verifyCode, String numberVerifyNumber) {
        if (TextUtils.isEmpty(username)) {
            UiUtil.showToast(R.string.error_tip_1);
            return;
        }
        if (!RegularUtils.isMobileSimple(username)) {
            UiUtil.showToast(R.string.error_tip_2);
            return;
        }
        if (TextUtils.isEmpty(verifyCode)) {
            UiUtil.showToast(R.string.error_tip_3);
            return;
        }
        numberVerifyNumber = "";
//        if (TextUtils.isEmpty(numberVerifyNumber)) {
//            UiUtil.showToast(R.string.error_tip_4);
//            return;
//        }
        mView.showProgress();
        HttpUtils.login(username, verifyCode, numberVerifyNumber)
                .compose(mView.<PersonalInfoBean>bind())
                .subscribe(new MySubscribe<PersonalInfoBean>() {
                    @Override
                    public void onNext(PersonalInfoBean personalInfoBean) {
                        mView.hideProgress();
                        if (personalInfoBean != null) {
                            if (personalInfoBean.isSuccess()) {
                                PersonalInfoBean.DataBean dataBean = personalInfoBean.getData();
                                if (dataBean != null) {
                                    UserInfoUtils.saveUserInfo(dataBean);
                                    SharedPreferencesUtil.getInstance().put(AppConstant.SpConstant.MOBILE_PHONE, username);
                                    mView.onLoginResult(true, dataBean.isNeedCompleteUserInfo());
                                    mView.showError(CoreLib.getContext().getString(R.string.login_success));
                                }
                            } else {
                                mView.showError(personalInfoBean.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.hideProgress();
                    }
                });
    }

    @Override
    public void getSMSVerifyCode(String phoneNumber) {
        HttpUtils.getSMSCode(phoneNumber)
                .compose(mView.<BaseModule>bind())
                .subscribe(new MySubscribe<BaseModule>() {
                    @Override
                    public void onNext(BaseModule baseModule) {
                        mView.onSendSMSVerifyCodeResult(baseModule.isSuccess());
                        if (!baseModule.isSuccess()) {
                            mView.showError(baseModule.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onSendSMSVerifyCodeResult(false);
                    }
                });
    }

    @Override
    public void getImageVerifyCode() {
        HttpUtils.getCheckVercode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscribe<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
//                        try {
//                            mView.onGetPicVerifyResult(responseBody.bytes());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
//                        LogUtils.i("操作失败，异常是：" + throwable + "  异常内容是：" + throwable.getMessage());
//                        if (throwable instanceof HttpException) {
//                            UiUtil.showToast("服务暂不可用");
//                        } else if (throwable instanceof IOException) {
//                            UiUtil.showToast("连接失败");
//                        } else {
//                            UiUtil.showToast("网络异常");
//                        }
                    }
                });
    }
}
