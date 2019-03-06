package com.cme.corelib.secret;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/4/28.
 * 加密相关的工具类
 */

public class SecurityUtils {
    /**
     * 获取通用签名头
     *
     * @param content
     * @return
     */
    public static String getSign(String content) {
        String result2 = null;
        try {
            byte[] result1 = Sha1.getHmacSHA1(content);
            result2 = BASE64.encode(result1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return result2;
    }
}
