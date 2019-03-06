package com.cme.coreuimodule.base.utils;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import com.cme.corelib.CoreLib;
import com.cme.corelib.utils.LogUtils;
import com.cme.corelib.utils.SharedPreferencesUtil;
import com.cme.corelib.utils.SizeUtils;
import com.cme.corelibmodule.R;
import com.cme.coreuimodule.base.activity.CommonBaseActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  1  固定显示个别字段颜色和大小
 *  2  转换H5格式
 * Created by xiaozi on 2018/3/1.
 */

public class TextCommon {
    public static SpannableStringBuilder pushContent(String title, String content) {
        String notice = "最新通知:";//前缀

        if (TextUtils.isEmpty(title)) {
            title = "";
        } else {
            title = "【" + title + "】";//标题
        }
        if (TextUtils.isEmpty(content)) {
            content = "";
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(notice + title + content);
        ssb.setSpan(new TextClickableSpan(0,true), 0,
                notice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new TextClickableSpan(1,true), notice.length(),
                notice.length() + title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        LogUtils.i(ssb.toString());
        return ssb;

    }
    public static SpannableStringBuilder pushContent(String title, String content,boolean isHead) {
        String notice = "最新通知:";//前缀

        if (TextUtils.isEmpty(title)) {
            title = "";
        } else {
            title = "【" + title + "】";//标题
        }
        if (TextUtils.isEmpty(content)) {
            content = "";
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(notice + title + content);
        ssb.setSpan(new TextClickableSpan(0,isHead), 0,
                notice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new TextClickableSpan(1,isHead), notice.length(),
                notice.length() + title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        LogUtils.i(ssb.toString());
        return ssb;

    }

    private static class TextClickableSpan extends ClickableSpan {

        private int type;//0--名字,1--整行评论
        private boolean isHead;//0--名字,1--整行评论

        public TextClickableSpan(int type,boolean isHead) {
            this.type = type;
            this.isHead=isHead;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            int i = SharedPreferencesUtil.getInstance().get(CommonBaseActivity.APP_TEXT_SIZE, 1);//获取字体基数
            int textSize = SizeUtils.sp2px(CoreLib.getContext(), 12 + 2 * i);//转换字体sp
            if (type == 0) {
                ds.setColor(CoreLib.getContext().getResources().getColor(R.color.invite_join_circle_select_tv_color));
                if (isHead) {
                ds.setTextSize(textSize);//动态设置字体大小级别
                }
                ds.setUnderlineText(false); // 去掉下划线
            } else if (type == 1) {
                ds.setColor(CoreLib.getContext().getResources().getColor(R.color.global_text_333));
                if (isHead) {
                ds.setTextSize(textSize);//动态设置字体大小级别
                }
                ds.setUnderlineText(false); // 去掉下划线
            }

        }

        @Override
        public void onClick(final View widget) {

        }

    }

    /**
     * 定义script的正则表达式
     */
    private static final String REGEX_SCRIPT = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    /**
     * 定义style的正则表达式
     */
    private static final String REGEX_STYLE = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    /**
     * 定义HTML标签的正则表达式
     */
    private static final String REGEX_HTML = "<[^>]+>";
    /**
     * 定义空格回车换行符
     */
    private static final String REGEX_SPACE = "\\s*|\t|\r|\n";

    /**
     * 处理特殊的H5字符
     * @param content
     * @return
     */
    public static String getAndroidText(String content){
        if (!TextUtils.isEmpty(content)) {
            // 过滤script标签
            Pattern p_script = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE);
            Matcher m_script = p_script.matcher(content);
            content = m_script.replaceAll("");
            // 过滤style标签
            Pattern p_style = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE);
            Matcher m_style = p_style.matcher(content);
            content = m_style.replaceAll("");
            // 过滤html标签
            Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
            Matcher m_html = p_html.matcher(content);
            content = m_html.replaceAll("");
//            // 过滤空格回车标签
//            Pattern p_space = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
//            Matcher m_space = p_space.matcher(content);
//            content = m_space.replaceAll("");
            //如果遇到未经处理的特殊字符本地做更改
            content= content.replace("lt;/divgt;","")
                    .replace("lt;divgt;","")
                    .replace("lt;brgt;","");
            return content.trim(); // 返回文本字符串
        }
         return "";
    }

    /***
     * 获取url 指定name的value;
     * @param url
     * @param name
     * @return
     */
    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }

}
