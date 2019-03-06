package com.cme.corelib.secret;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 作者：Android_AJ on 2017/4/26.
 * 邮箱：ai15116811712@163.com
 * 版本：v1.0
 */
public class Sha1 {
    /**
     * SHA加密
     *
     * @param strSrc 明文
     * @return 加密之后的密文
     */


    public static String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts 数据源
     * @return 16进制字符串
     */
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    public static byte[] getHmacSHA1(String src)
            throws NoSuchAlgorithmException, UnsupportedEncodingException,
            InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret = new SecretKeySpec(CoreConstant.SIGNKEY.getBytes("UTF-8"), mac.getAlgorithm());
        mac.init(secret);
        return mac.doFinal(src.getBytes("UTF-8"));
    }
}
