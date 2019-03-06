package com.cme.corelib.http;

/**
 * 作者：Android_AJ on 2017/4/28.
 * 邮箱：ai15116811712@163.com
 * 版本：v1.0
 * 方法名常量类  未特殊声明，是get请求
 * 说明： 40003 方法未定义  40008 signature验证失败   invalid_session 无效session
 */
public interface Methods {
    // 公共接口
    String image_upload = "/cms-filenfsPro-app/upload/uploadFile";
    String image_show = "/cms-filenfsPro-app/showimg/";

    // 登录
    String methods_1 = "/get-vcode.json";  // 获取短信验证码
    String methods_2 = "/get-pcode.json";  // 获取验证码
    String methods_3 = "/login.json";  // 登录
    String methods_4 = "/complete-info.json?";  // 完善用户资料

    // 更新
    String method_get_version="/android/get-version-info.json";  // 获取最新版本信息
}
