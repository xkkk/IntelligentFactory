package com.cmeplaza.intelligentfactory.utils;

/**
 * Created by klx on 2018/5/10.
 * app常量类
 */

public interface AppConstant {
    String CONFIG_PROPERTIES = "/assets/config_release.properties";
    String CONFIG_PROPERTIES_DEV = "/assets/config_dev.properties";
    String CONFIG_PROPERTIES_DEBUG = "/assets/config_debug.properties";

    String BASE_URL = AppStringUtils.getConfigProperties("URL_API");  // 接口地址
    String BASE_H5_URL = AppStringUtils.getConfigProperties("URL_H5");  // h5地址

    /**
     * sp用到的常量
     */
    interface SpConstant {
        String USER_FIRST_IN = "user_first_in";  // 用户第一次进入应用
        String MOBILE_PHONE = "mobile_phone";  // 手机号

        String USER_ID = "user_id";  // 用户ID
        String USER_INFO = "USER_INFO";  // 用户信息
    }

}
