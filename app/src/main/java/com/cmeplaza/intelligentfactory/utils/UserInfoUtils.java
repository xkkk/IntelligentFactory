package com.cmeplaza.intelligentfactory.utils;

import com.cme.corelib.utils.SharedPreferencesUtil;
import com.cmeplaza.intelligentfactory.login.bean.PersonalInfoBean;

/**
 * Created by klx on 2018/5/11.
 * 用户信息工具类
 */

public class UserInfoUtils {
    public static void saveUserInfo(PersonalInfoBean.DataBean dataBean) {
        if (dataBean == null) {
            return;
        }
        SharedPreferencesUtil.getInstance().saveJson(AppConstant.SpConstant.USER_INFO, dataBean);
        SharedPreferencesUtil.getInstance().put(AppConstant.SpConstant.USER_ID, dataBean.getUserId());
    }

    public static PersonalInfoBean.DataBean getUserInfo() {
        return (PersonalInfoBean.DataBean) SharedPreferencesUtil.getInstance().getFromJson(AppConstant.SpConstant.USER_INFO, PersonalInfoBean.DataBean.class);
    }

    public static String getUserId(){
        return SharedPreferencesUtil.getInstance().get(AppConstant.SpConstant.USER_ID);
    }

    public static void clearUserInfo() {
        SharedPreferencesUtil.getInstance().clearJson(AppConstant.SpConstant.USER_INFO);
        SharedPreferencesUtil.getInstance().put(AppConstant.SpConstant.USER_ID, "");
    }
}
