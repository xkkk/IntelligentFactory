package com.cme.corelib.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：xk on 2017/9/16
 * 版本：v1.0
 * 描述：
 */

public class ErrorCode {

    public static final Map<String, String> errorCodeMap = new HashMap<>();

    // 错误码
    static {
        // 应用部分
        errorCodeMap.put(" 50006", "未登录");
    }
}
