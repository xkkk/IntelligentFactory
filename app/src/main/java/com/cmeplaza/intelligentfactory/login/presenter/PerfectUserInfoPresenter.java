package com.cmeplaza.intelligentfactory.login.presenter;

import android.text.TextUtils;

import com.cme.corelib.CoreLib;
import com.cme.corelib.bean.BaseModule;
import com.cme.corelib.http.MySubscribe;
import com.cme.corelib.utils.UiUtil;
import com.cme.coreuimodule.base.mvp.RxPresenter;
import com.cmeplaza.intelligentfactory.R;
import com.cmeplaza.intelligentfactory.login.bean.PersonalInfoBean;
import com.cmeplaza.intelligentfactory.login.contract.PerfectUserInfoContract;
import com.cmeplaza.intelligentfactory.utils.HttpUtils;
import com.cmeplaza.intelligentfactory.utils.UserInfoUtils;

/**
 * Created by klx on 2018/5/11.
 * 完善个人信息presenter
 */

public class PerfectUserInfoPresenter extends RxPresenter<PerfectUserInfoContract.IView> implements PerfectUserInfoContract.IPresenter {
    @Override
    public void editUserInfo(String card, String invitationCode, String niceName, String trueName) {
//        if (TextUtils.isEmpty(card)) {
//            UiUtil.showToast(CoreLib.getContext().getString(R.string.edit_info_error_tip_1));
//            return;
//        }
//        if (!(RegularUtils.isIDCard15(card) || RegularUtils.isIDCard18(card))) {
//            UiUtil.showToast(CoreLib.getContext().getString(R.string.edit_info_error_tip_2));
//            return;
//        }
        if (TextUtils.isEmpty(niceName)) {
            UiUtil.showToast(CoreLib.getContext().getString(R.string.edit_info_error_tip_4));
            return;
        }
//        if (TextUtils.isEmpty(trueName)) {
//            UiUtil.showToast(CoreLib.getContext().getString(R.string.edit_info_error_tip_3));
//            return;
//        }
        mView.showProgress();
        if (TextUtils.isEmpty(invitationCode)) {
            invitationCode = "";
        }
        HttpUtils.editUserData(card, invitationCode, niceName, trueName)
                .compose(mView.<BaseModule>bind())
                .subscribe(new MySubscribe<BaseModule>() {
                    @Override
                    public void onNext(BaseModule baseModule) {
                        mView.hideProgress();
                        if (baseModule.isSuccess()) {
                            if (UserInfoUtils.getUserInfo() != null) {
                                PersonalInfoBean.DataBean dataBean = UserInfoUtils.getUserInfo();
                                dataBean.setCompleteUserInfo();
                                UserInfoUtils.saveUserInfo(dataBean);
                            }
                            mView.onEditSuccess();
                        } else {
                            mView.showError(baseModule.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.hideProgress();
                    }
                });
    }
}
