package com.cme.coreuimodule.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.cme.corelib.utils.SizeUtils;
import com.common.coreuimodule.R;

/**
 * Created by klx on 2017/7/24.
 * 步骤progressView  #f6cc59
 */

public class StepProgressView extends View {
    private String defaultTextColor = "#f6cc59";
    // 控件的宽高
    private int height;
    private int width;
    // 当前进度
    private float progress = 0;
    // 文本颜色
    private int textColor = Color.parseColor(defaultTextColor);
    private int secondColor = Color.parseColor(defaultTextColor);
    private int firstColor = Color.parseColor("#2D4C69");

    // 开始和结束进度
    private int startText = 0;
    private int endText = 100;
    private int totalCount = 5;

    private Paint firstPaint;
    private Paint secondPaint;
    private Paint textPaint;

    private Bitmap bgBitmap;

    public StepProgressView(Context context) {
        this(context, null);
    }

    public StepProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StepProgressView);
        secondColor = a.getColor(R.styleable.StepProgressView_secondProgressColor, Color.parseColor(defaultTextColor));
        int id_bg = a.getResourceId(R.styleable.StepProgressView_secondProgress_bg, 0);
        if (id_bg > 0) {
            bgBitmap = BitmapFactory.decodeResource(context.getResources(), id_bg);
        }
        a.recycle();
        init();
    }

    private void init() {
        firstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        firstPaint.setColor(firstColor);
        firstPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        firstPaint.setStrokeCap(Paint.Cap.ROUND);

        secondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondPaint.setColor(secondColor);
        secondPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        secondPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(SizeUtils.sp2px(getContext(), 6));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = new RectF(0, height / 6, width, height / 2);
//        // 画底部
        if (bgBitmap != null) {
            canvas.drawBitmap(bgBitmap, 10, 10, firstPaint);
        } else {
            canvas.drawRoundRect(rect, 10, 10, firstPaint);
        }
        // 画进度
        rect.set(0, height / 6, (int) (width * (progress / endText)), height / 2);
        canvas.drawRoundRect(rect, 10, 10, secondPaint);
        // 画文本
        int step = (endText - startText) / totalCount;
        int stepWidth = width / totalCount;
        for (int i = 0; i < totalCount; i++) {
            String text = String.valueOf(startText + i * step);
            text = limitText(text);
            if (i == 0) {
                canvas.drawText(text, i * stepWidth, height, textPaint);
            } else {
                canvas.drawText(text, i * stepWidth - textPaint.measureText(text) / 3, height, textPaint);
            }
        }
    }

    /**
     * 设置进度值
     *
     * @param progress
     */
    public void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        }
        setStartAndEndValue(progress);
        this.progress = progress;
        invalidate();
    }

    public void addProgress(int addProgress) {
        setProgress((int) (this.progress + addProgress));
    }

    public float getProgress() {
        return this.progress;
    }

    private void setStartAndEndValue(int evolutionary) {
        if (evolutionary < 100) {   //智人
            startText = 0;
            endText = 100;
        } else if (evolutionary >= 100 && evolutionary < 500) {//智人
            startText = 100;
            endText = 500;
        } else if (evolutionary >= 500 && evolutionary < 3000) {//圈主
            startText = 500;
            endText = 3000;
        } else if (evolutionary >= 3000 && evolutionary < 10000) {//宗主
            startText = 3000;
            endText = 10000;
        } else if (evolutionary >= 10000 && evolutionary < 30000) {//盟主
            startText = 10000;
            endText = 30000;
        } else if (evolutionary >= 30000) {//酋长
            startText = 30000;
            endText = 80000;
        }
    }

    private String limitText(String text) {
        float intValue = Float.parseFloat(text);
        if (intValue >= 1000) {
            intValue = intValue / 1000;
            return intValue + "k";
        }
        return text;
    }
}
