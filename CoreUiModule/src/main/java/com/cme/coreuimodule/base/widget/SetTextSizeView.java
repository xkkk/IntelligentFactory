package com.cme.coreuimodule.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cme.corelib.utils.SizeUtils;
import com.common.coreuimodule.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Android_AJ on 2017/4/10.
 * 邮箱：ai15116811712@163.com
 * 版本：v1.0
 * 仿微信设置字体大小的view
 * 自定义属性有：1.一共多少格 2.线条颜色与粗细 3.圆的半径和颜色
 */
public class SetTextSizeView extends View {

    private int defaultLineColor = Color.rgb(33, 33, 33);
    private int defaultLineWidth;
    private int defaultMax = 5;
    private int defaultCircleColor = Color.WHITE;
    private int defaultCircleRadius;
    private int defaultPosition = 1;

    // 一共有多少格
    private int max = 5;
    // 线条颜色
    private int lineColor;
    // 线条粗细
    private int lineWidth;
    // 突出部分的线条高度
    private int lineHeight;
    // 圆半径
    private int circleRadius;
    private int circleColor;
    // 一段的宽度，根据总宽度和总格数计算得来
    private int itemWidth;
    // 控件的宽高
    private int height;
    private int width;
    // 当前所在位置
    private int currentProgress = defaultPosition;
    // 小字体文本
    private String minText = "A";
    // 大字体文本
    private String maxText = "A";
    // 标准字体文本
    private String standardText = "标准";
    // 文本颜色
    private int textColor = Color.BLACK;
    // 画笔
    private Paint mLinePaint;
    private Paint mCirclePaint;
    private Paint mTextPaint;
    // 滑动过程中x坐标
    private float currentX = 0;
    // 有效数据点
    private List<Point> points = new ArrayList<>();

    private float circleX;
    private float circleY;

    public SetTextSizeView(Context context) {
        this(context, null);
    }

    public SetTextSizeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // initDefault
        defaultLineWidth = dp2px(context, 2);
        defaultCircleRadius = dp2px(context, 35);

        lineColor = Color.rgb(33, 33, 33);
        lineWidth = dp2px(context, 2);
        circleColor = Color.WHITE;

        // initCustomAttrs
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SetTextSizeView);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();

        // 初始化画笔
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(lineColor);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setStrokeWidth(lineWidth);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(circleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);
        // 设置阴影效果
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mCirclePaint.setShadowLayer(2, 0, 0, Color.rgb(33, 33, 33));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.SetTextSizeView_lineColor) {
            lineColor = typedArray.getColor(attr, defaultLineColor);
        } else if (attr == R.styleable.SetTextSizeView_circleColor) {
            circleColor = typedArray.getColor(attr, defaultCircleColor);
        } else if (attr == R.styleable.SetTextSizeView_lineWidth) {
            lineWidth = typedArray.getDimensionPixelSize(attr, defaultLineWidth);
        } else if (attr == R.styleable.SetTextSizeView_circleRadius) {
            circleRadius = typedArray.getDimensionPixelSize(attr, defaultCircleRadius);
        } else if (attr == R.styleable.SetTextSizeView_totalCount) {
            max = typedArray.getInteger(attr, defaultMax);
        } else if (attr == R.styleable.SetTextSizeView_minText) {
            minText = typedArray.getString(attr);
        } else if (attr == R.styleable.SetTextSizeView_maxText) {
            maxText = typedArray.getString(attr);
        } else if (attr == R.styleable.SetTextSizeView_standardText) {
            standardText = typedArray.getString(attr);
        } else if (attr == R.styleable.SetTextSizeView_standardPosition) {
            defaultPosition = typedArray.getInteger(attr, 1);
        } else if (attr == R.styleable.SetTextSizeView_textColor) {
            textColor = typedArray.getColor(attr, Color.BLACK);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
        circleY = height - height / 4;
        lineHeight = height / 8;
        // 横线宽度是总宽度-2个圆的半径
        itemWidth = (w - 2 * circleRadius) / max;
        // 把可点击点保存起来
        for (int i = 0; i <= max; i++) {
            points.add(new Point(circleRadius + i * itemWidth, height / 2));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 先画中间的横线
        canvas.drawLine(points.get(0).x, height - height / 4, points.get(points.size() - 1).x, height - height / 4, mLinePaint);
        // 绘制刻度
        for (Point point : points) {
            canvas.drawLine(point.x, height - (height / 4 - lineHeight), point.x, height - (height / 4 + lineHeight), mLinePaint);
        }
        // 画圆
        if (canMove) {
            // 随手指滑动过程
            if (currentX < circleRadius) {
                currentX = circleRadius;
            }
            if (currentX > width - circleRadius) {
                currentX = width - circleRadius;
            }
            circleX = currentX;
        } else {
            // 最终
            if (currentProgress == 0) {
                circleX = points.get(currentProgress).x + lineWidth;
            } else {
                circleX = points.get(currentProgress).x;
            }
        }
        // 实体圆
        canvas.drawCircle(circleX, circleY, circleRadius, mCirclePaint);

        // 画文本
        mTextPaint.setTextSize(SizeUtils.sp2px(getContext(),12));
        canvas.drawText(minText, circleRadius, height / 4, mTextPaint);
        mTextPaint.setTextSize(SizeUtils.sp2px(getContext(),14));
        canvas.drawText(standardText, circleRadius + itemWidth - mTextPaint.measureText(standardText) / 2, height / 4, mTextPaint);
        mTextPaint.setTextSize(SizeUtils.sp2px(getContext(),18));
        canvas.drawText(maxText, width - 2 * circleRadius, height / 4, mTextPaint);
    }

    float downX = 0;
    private boolean canMove = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 判断是否是数据点
                downX = event.getX();
                canMove = isDownOnCircle(downX);
                break;
            case MotionEvent.ACTION_MOVE:
                if (canMove) {
                    currentX = event.getX();
                    if (isOnPoint(currentX)) {
                        if (onPointResultListener != null) {
                            onPointResultListener.onPointResult(currentProgress);
                        }
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起之后就圆就不能在非有效点
                currentX = 0;
                float upX = event.getX();
                if (canMove) {
                    // 是滑动过来的，要判断距离哪个有效点最近，就滑动到哪个点
                    Point targetPoint = getNearestPoint(upX);
                    if (targetPoint != null) {
                        invalidate();
                    }
                } else {
                    if (Math.abs(downX - upX) < 30) {
                        Point point = isValidPoint(upX);
                        if (point != null) {
                            invalidate();
                        }
                    }
                }
                if (onPointResultListener != null) {
                    onPointResultListener.onPointResult(currentProgress);
                }
                downX = 0;
                canMove = false;
                break;
        }
        return true;
    }

    /**
     * 滑动抬起之后，要滑动到最近的一个点那里
     *
     * @param x
     * @return
     */
    private Point getNearestPoint(float x) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (Math.abs(point.x - x) < itemWidth / 2) {
                currentProgress = i;
                return point;
            }
        }
        return null;
    }

    /**
     * 判断是否点击到圆上
     *
     * @param x
     * @return
     */
    private boolean isDownOnCircle(float x) {
        return Math.abs(points.get(currentProgress).x - x) < circleRadius;
    }

    /**
     * 判断是否是有效的点击点
     *
     * @param x
     */
    private Point isValidPoint(float x) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (Math.abs(point.x - x) < 30) {
                currentProgress = i;
                return point;
            }
        }
        return null;
    }

    private boolean isOnPoint(float x) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (Math.abs(point.x - x) < circleRadius) {
                currentProgress = i;
                return true;
            }
        }
        return false;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setDefaultPosition(int position) {
        currentProgress = position;
        invalidate();
    }

    public void setOnPointResultListener(OnPointResultListener onPointResultListener) {
        this.onPointResultListener = onPointResultListener;
    }

    private OnPointResultListener onPointResultListener;

    public interface OnPointResultListener {
        void onPointResult(int position);
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    public static int px2sp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scale + 0.5f);
    }
}
