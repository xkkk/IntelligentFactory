package com.cme.corelib.http.retrofit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by klx on 2017/9/16.
 */

public class ErrorCodeMap {
    public static final Map<String, String> errorCodeMap = new HashMap<>();

    // 错误码
    static {
        // 我的部分
        errorCodeMap.put("1001", "登录状态失效，请重新登录");
        errorCodeMap.put("1006", "帐户未找到");
        errorCodeMap.put("1008", "您输入的密码有误");
        errorCodeMap.put("1009", "帐户被禁用");
    }
}
