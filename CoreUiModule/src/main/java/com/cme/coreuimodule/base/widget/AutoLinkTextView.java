package com.cme.coreuimodule.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cme.corelib.utils.RegularUtils;
import com.common.coreuimodule.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by klx on 2017/9/8.
 * 自动显示链接的TextView  可以是：手机号|链接等
 */

public class AutoLinkTextView extends AppCompatTextView {
    // 手机号的正则表达式
//    private static final String REGEX_MOBILE_SIMPLE = "(1|861)(3|5|8)\\d{9}$*";
    private static final String REGEX_MOBILE_SIMPLE = RegularUtils.REGEX_INTERNET_URL;
    // 特殊字符显示的颜色
    private int specialTextColor = Color.BLUE;

    public AutoLinkTextView(Context context) {
        this(context,null);
    }

    public AutoLinkTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AutoLinkTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoLinkTextView);
        specialTextColor = a.getColor(R.styleable.AutoLinkTextView_specialTextColor,Color.BLUE);
        a.recycle();
    }

    public void setText(String text) {
        if (text == null || text.length() == 0) return;
        Object[][] output = getUrlFromJDP(text);
        int urlCount = 0;
        if (output == null || output.length == 0 || output[0] == null || output[0].length == 0) {
            super.setText(text);
            return;
        }
        urlCount = output[0].length;
        Log.i("AlexUrl", "一共有" + urlCount + "个url");
        String remainText = text;
        int lastStart = 0;//截取到一部分后截掉部分的长度
        for (int i = 0; i < urlCount; i++) {
            final String blueText = (String) output[0][i];//带下划线的文字
            final String href = (String) output[1][i];//下划线文字所对应的url连接
            int start = (int) output[2][i];//<a>标签在源字符串的起始位置
            int end = (int) output[3][i];//<a>标签在源字符串的结束位置
            SpannableString spannableString = new SpannableString(blueText);
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(specialTextColor);
                }

                //在这里定义点击下划线文字的点击事件，不一定非要打开浏览器
                @Override
                public void onClick(View widget) {
                    if (onSpecialTextClickListener != null) {
                        onSpecialTextClickListener.onSpecialTextClick(blueText);
                    }
                    widget.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (onSpecialTextClickListener != null) {
                                onSpecialTextClickListener.onSpecialTextLongClick(blueText);
                                return true;
                            }
                            return false;
                        }
                    });
                }
            }, 0, blueText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            int subStart = start - lastStart;
            String front = remainText.substring(0, subStart);//截取出一段文字+一段url
            Log.i("Alex", "起始位置" + (end - lastStart));
            remainText = remainText.substring(end - lastStart, remainText.length());//剩下的部分
            lastStart = end;
            Log.i("Alex", "front是" + front);
            Log.i("Alex", "spann是" + spannableString);
            Log.i("Alex", "remain是" + remainText);
            if (front.length() > 0) append(front);
            append(spannableString);
        }
        if (remainText != null && remainText.length() > 0) append(remainText);
        setMovementMethod(LinkMovementMethod.getInstance());//响应点击事件
    }

    public static Object[][] getUrlFromJDP(String source) {
        ArrayList<String> hosts = new ArrayList<>(4);
        ArrayList<String> urls = new ArrayList<>(4);
        ArrayList<Integer> starts = new ArrayList<>(4);
        ArrayList<Integer> ends = new ArrayList<>(4);
        Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);//首先将a标签分离出来
        Matcher matcher = pattern.matcher(source);
        int i = 0;
        while (matcher.find()) {
            String raw = matcher.group(0);
            Pattern url_pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);//将href分离出来
            Matcher url_matcher = url_pattern.matcher(raw);
            try {
                if (url_matcher.find()) {
                    String url = url_matcher.group(0);
                    Log.i("Alex", "真实url是" + url);//括号里面的
                    urls.add(i, url);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String host = null;//将要显示的文字分离出来
            try {
                host = matcher.group(0);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Log.i("Alex", "蓝色文字是" + host);//括号里面的
            hosts.add(i, host);
            starts.add(i, matcher.start());
            ends.add(i, matcher.end());
            Log.i("Alex", "字符串起始下标是" + matcher.start() + "结尾下标是" + matcher.end());//匹配出的字符串在源字符串的位置
            i++;
        }
        if (hosts.size() == 0) {
            Log.i("Alex", "没有发现url");
            return null;
        }
        Object[][] outputs = new Object[4][hosts.size()];//第一个下标是内容的分类，第二个下标是url的序号
        outputs[0] = hosts.toArray(new String[hosts.size()]);//下标0是蓝色的文字
        outputs[1] = urls.toArray(new String[urls.size()]);//下标1是url
        outputs[2] = starts.toArray(new Integer[starts.size()]);//下标2是<a>标签起始位置
        outputs[3] = ends.toArray(new Integer[ends.size()]);//下标3是<a>标签结束位置
        return outputs;
    }

    /**
     * 设置特殊字符点击效果
     * @param onSpecialTextClickListener
     */
    public void setOnSpecialTextClickListener(OnSpecialTextClickListener onSpecialTextClickListener) {
        this.onSpecialTextClickListener = onSpecialTextClickListener;
    }

    OnSpecialTextClickListener onSpecialTextClickListener;

    public interface OnSpecialTextClickListener {
        void onSpecialTextClick(String text);
        void onSpecialTextLongClick(String text);
    }
}
