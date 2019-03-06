package com.cme.coreuimodule.base.widget.filter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.cme.corelib.utils.UiUtil;
import com.common.coreuimodule.R;

/**
 * 限制系统表情输入
 * Created by xiaozi  on 2017/6/19.
 */

public class MyEmojiEditText extends AppCompatEditText {
    private int maxEditTextLength;
    public MyEmojiEditText(Context context) {
        this(context, null);
    }
    public MyEmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取定义的属性
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyEmojiEditText);
        // EditText最多可输入的长度
        maxEditTextLength = a.getInteger(R.styleable.MyEmojiEditText_emoji_max_length, 200);
        a.recycle();
        InputFilter[] filters = {Filter.emojiFilter, new InputFilter.LengthFilter(maxEditTextLength) {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder tempResult = new StringBuilder(source).append(dest);
                if (!TextUtils.isEmpty(tempResult.toString()) && tempResult.toString().length() > maxEditTextLength) {
                    UiUtil.showToast("最多输入" + maxEditTextLength + "个字");
                }
                return super.filter(source, start, end, dest, dstart, dend);
            }
        }};
        setFilters(filters);
    }
}
