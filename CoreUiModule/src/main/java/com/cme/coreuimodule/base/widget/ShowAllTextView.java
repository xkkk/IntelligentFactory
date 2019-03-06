package com.cme.coreuimodule.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.common.coreuimodule.R;


/**
 * Created by klx on 2017/8/25.
 * 单行，显示所有Text的控件，自动调整文本大小
 */

public class ShowAllTextView extends AppCompatTextView {
    // 最小文本大小
    private int minTextSize;

    public ShowAllTextView(Context context) {
        this(context, null);
    }

    public ShowAllTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowAllTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShowAllTextView);
        int minTextSizeSp = a.getInt(R.styleable.ShowAllTextView_minTextSize, 12);
        minTextSize = spToPx(minTextSizeSp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setAutoText(final CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        float textWidth = getPaint().measureText(text.toString());


        //可显示文本区域的宽度
        int availableTextWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        if (availableTextWidth <= 0) {
            int resultWidth = 0;
            Drawable[] drawables = getCompoundDrawables();
            if (drawables.length > 0) {
                Drawable drawableLeft = drawables[0];
                if (drawableLeft != null) {
                    resultWidth += drawableLeft.getIntrinsicWidth();
                }
            }
            resultWidth += textWidth;
            resultWidth += getCompoundDrawablePadding();
            availableTextWidth = resultWidth;
        }
        if (minTextSize <= getTextSize()) {
            while (textWidth > availableTextWidth) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize() - 1);
                textWidth = getPaint().measureText(text.toString());
            }
        }
        setText(text);
    }

    private int spToPx(int sp) {
        float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }
}
