package com.cme.corelib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.cme.corelib.image.ImageLoaderManager;
import com.cme.corelib.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klx on 2017/9/1.
 * 配置类
 */

public class CoreLib {
    // 聊天
    public static List<Activity> activityList = new ArrayList<>();
    public static String PROJECT_NAME = "/smart-factory-app";
    public static String BASE_H5_URL;
    public static String BASE_FILE_URL;
    private static Application mContext;
    private static String BASE_URL;

    public static boolean isResume = false;

    public static void init(Application context) {
        mContext = context;
        ImageLoaderManager.getInstance().init(context);
    }

    public static void initNet(String baseUrl, String baseH5Url) {
        BASE_URL = baseUrl;
        BASE_H5_URL = baseH5Url;
    }

    public static void setBaseFileUrl(String baseFileUrl) {
        BASE_FILE_URL = baseFileUrl;
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getBaseUrl() {
        if (TextUtils.isEmpty(BASE_URL)) {
            throw new RuntimeException("You should call initNet method first");
        }
        return BASE_URL;
    }

    public static String getCurrentUserId() {
        return SharedPreferencesUtil.getInstance().get("userId");
    }

    public static void setCurrentUserId(String userId) {
        SharedPreferencesUtil.getInstance().put("userId", userId);
    }

    public static String getCurrentUserPhone() {
        return SharedPreferencesUtil.getInstance().get("userPhone");
    }

    public static void setCurrentUserPhone(String userPhone) {
        SharedPreferencesUtil.getInstance().put("userPhone", userPhone);
    }

    public static String getCurrentUserName() {
        return SharedPreferencesUtil.getInstance().get("userName");
    }

    public static void setCurrentUserName(String userName) {
        SharedPreferencesUtil.getInstance().put("userName", userName);
    }

    public static String getCurrentUserPortrait() {
        return SharedPreferencesUtil.getInstance().get("UserPortrait");
    }

    public static void setCurrentUserPortrait(String userName) {
        SharedPreferencesUtil.getInstance().put("UserPortrait", userName);
    }

    public static String getSession() {
        return SharedPreferencesUtil.getInstance().get("session");
    }

    public static void setSession(String session) {
        SharedPreferencesUtil.getInstance().put("session", session);
    }

    /**
     * 清空登录用户信息
     */
    public static void clearUserCache() {
        SharedPreferencesUtil.getInstance().put("userId", "");
        SharedPreferencesUtil.getInstance().put("userName", "");
        SharedPreferencesUtil.getInstance().put("userPhone", "");

        SharedPreferencesUtil.getInstance().put("password", "");
        SharedPreferencesUtil.getInstance().put("session", "");
        SharedPreferencesUtil.getInstance().clearJson("userInfo");
        SharedPreferencesUtil.getInstance().put("register_umeng_success", false);
        SharedPreferencesUtil.getInstance().clearJson("PersonalInfoBean");
    }
}
