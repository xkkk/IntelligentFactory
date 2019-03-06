package com.cme.corelib.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 作者：Android_AJ on 2017/4/11.
 * 邮箱：ai15116811712@163.com
 * 版本：v1.0
 * 与字符串相关的工具类
 */
public class StringUtils {
    /**
     * 隐藏手机号中间的4位
     */
    public static String hidePhoneNum(String phoneNum) {
        StringBuilder result = new StringBuilder();
        if (!TextUtils.isEmpty(phoneNum) && phoneNum.length() > 6) {
            for (int i = 0; i < phoneNum.length(); i++) {
                char c = phoneNum.charAt(i);
                if (i >= 3 && i <= 6) {
                    result.append('*');
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }

    public static String urlEncode(String signature) {
        String encodeSign = null;
        try {
            encodeSign = URLEncoder.encode(signature, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeSign;
    }

    /**
     * 删除Html标签
     *
     * @param inputString
     * @return
     */
    public static String htmlRemoveTag(String inputString) {
        if (inputString == null)
            return null;
        String htmlStr = inputString; // 含html标签的字符串
        String textStr;
        String regEx_script = "<[^>]*>|&nbsp;";
        Pattern p_script;
        p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher m_script;
        m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        textStr = htmlStr.replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\\")
                .replace("&apos;", "\'");
        return textStr;// 返回文本字符串
    }


    public static String getPropertierString(Context context, String fileName, String keyName) {
        String strValue = "";
        Properties props = new Properties();
        try {
            props.load(context.getAssets().open(fileName));
            strValue = props.getProperty(keyName);
            System.out.println(keyName + " " + strValue);
        } catch (FileNotFoundException e) {
            Log.e("StringUtils", "config.properties Not Found Exception", e);
        } catch (IOException e) {
            Log.e("StringUtils", "config.properties IO Exception", e);
        }
        return strValue;
    }


    /**
     * 判断文件是否是图片
     *
     * @param fileName 文件名
     * @return
     */
    public static boolean isPicture(String fileName) {
        return !TextUtils.isEmpty(fileName)
                && (fileName.endsWith(".jpg") || fileName.endsWith(".JPG")
                || fileName.endsWith(".png")
                || fileName.endsWith(".PNG")
                || fileName.endsWith(".jpeg") || fileName
                .endsWith(".bmp"));
    }

    /**
     * 判断文件是否是图片
     *
     * @param fileName 文件名
     * @return
     */
    public static boolean isVideo(String fileName) {
        return !TextUtils.isEmpty(fileName)
                && (fileName.endsWith(".mp4") || fileName.endsWith(".m4v")
                || fileName.endsWith(".mov")
                || fileName.endsWith(".3gp")
                || fileName.endsWith(".3gpp") || fileName
                .endsWith(".avi"));
    }

    public static String getDayBeforeNow(long time) {
        long day = (System.currentTimeMillis() - time) / (24 * 60 * 60 * 1000);
        return String.valueOf(day);
    }

    public static String getHostName(String downurl) {
        String head = "";
        int index = downurl.indexOf("://");
        if (index != -1) {
            head = downurl.substring(0, index + 3);
            downurl = downurl.substring(index + 3);
        }
        index = downurl.indexOf("/");
        if (index != -1) {
            downurl = downurl.substring(0, index + 1);
        }
        return head + downurl;
    }

    public static String getDataSize(long var0) {
        DecimalFormat var2 = new DecimalFormat("###.00");
        return var0 < 1024L ? var0 + "bytes" : (var0 < 1048576L ? var2.format((double) ((float) var0 / 1024.0F))
                + "KB" : (var0 < 1073741824L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F))
                + "MB" : (var0 < 0L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F / 1024.0F))
                + "GB" : "error")));
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!");//替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String getFilterString(String input){
        return StringFilter(ToDBC(input));
    }

}

