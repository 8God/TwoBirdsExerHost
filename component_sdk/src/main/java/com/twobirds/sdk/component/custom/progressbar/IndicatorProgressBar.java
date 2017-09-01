package com.twobirds.sdk.component.custom.progressbar;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.twobirds.sdk.component.R;


/**
 * |  version  |  author  |  version log
 * |   0.0.1   |   CTH    |  初始化
 * |   0.0.2   |   CTH    |  修复Android Studio 预览与真机运行不一致问题
 * |   0.0.3   |   CTH    |  1. 设置Progress时更新全局变量indicatorTextLength；2. 提示圆点和提示文字适应padding
 * <p>
 * </br>
 * 带指示器和指示数字的进度条
 *
 * @author CTH
 * @version 0.0.2
 */

public class IndicatorProgressBar extends ProgressBar {

    private static final String TAG = "IndicatorProgressBar";


    private int measureHeight;
    private int measureWidth;

    private Paint indicatorPaint;  //指示器（圆点）画笔
    private Paint indicatorTextPaint; //指示数字画笔

    /* 指示数字大小 */
    private float indicatorTextSize = dip2px(18);
    /* 指示数字颜色 */
    private int indicatorTextColor = getResources().getColor(R.color.colorAccent);
    /* 指示器（圆点）颜色 */
    private int indicatorColor = getResources().getColor(R.color.colorAccent);
    /* 指示器（圆点）颜色大小（半径） */
    private float indicatorRadius = dip2px(7);
    /* 进度条高度 */
    private float progressBarHeight = dip2px(20);
    /* 指示文字垂直偏移量 */
    private float indicatorTextYOffset = dip2px(18);

    private int indicatorTextLength = 0;

    private final float MIN_PROGRESSBAR_HEIGHT = progressBarHeight;

    public IndicatorProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public IndicatorProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IndicatorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initPaint();

        updateProgressTextLength();
    }

    /**
     * 获取xml的自定义控件属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorProgressBar);
            if (null != typedArray) {
                indicatorTextSize = typedArray.getDimension(R.styleable.IndicatorProgressBar_indicator_textSize, indicatorTextSize);
                indicatorTextColor = typedArray.getColor(R.styleable.IndicatorProgressBar_indicator_textColor, indicatorTextColor);
                indicatorRadius = typedArray.getDimension(R.styleable.IndicatorProgressBar_indicator_radius, indicatorRadius);
                indicatorColor = typedArray.getColor(R.styleable.IndicatorProgressBar_indicator_color, indicatorColor);
                progressBarHeight = typedArray.getDimension(R.styleable.IndicatorProgressBar_progressBar_height, progressBarHeight);
                indicatorTextYOffset = typedArray.getDimension(R.styleable.IndicatorProgressBar_indicator_text_yoffset, indicatorTextYOffset);

                progressBarHeight = progressBarHeight >= MIN_PROGRESSBAR_HEIGHT ? progressBarHeight : MIN_PROGRESSBAR_HEIGHT;

                typedArray.recycle();
            }
        }
    }

    /**
     * 实例化画笔
     */
    private void initPaint() {
        indicatorPaint = new Paint();
        indicatorPaint.setAntiAlias(true);
        indicatorPaint.setColor(indicatorColor);
        indicatorPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        indicatorTextPaint = new Paint();
        indicatorTextPaint.setAntiAlias(true);
        indicatorTextPaint.setColor(indicatorTextColor);
        indicatorTextPaint.setTextSize(indicatorTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        measureWidth = MeasureSpec.getSize(widthMeasureSpec);

        measureHeight = (int) (progressBarHeight + indicatorTextYOffset + indicatorTextSize + getPaddingTop() + getPaddingBottom());
        setMeasuredDimension(measureWidth, measureHeight);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Drawable progressDrawable = getProgressDrawable();
        if (null != progressDrawable) {
            //设置progressDrawable的边界
            progressDrawable.setBounds(0, 0, measureWidth - getPaddingLeft() - getPaddingRight(), (int) progressBarHeight);
//            progressDrawable.setBounds(getPaddingLeft(), getPaddingTop(), measureWidth - getPaddingRight(), (int) (progressBarHeight - getPaddingBottom()));
        }
    }

    private void updateProgressTextLength() {

        int progress = getProgress();

        progress = progress < 0 ? 0 : progress;

        StringBuilder sb = new StringBuilder();
        sb.append(progress);

        indicatorTextLength = sb.length();

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int progressDrawableWidth = measureWidth - getPaddingLeft() - getPaddingRight();

        Drawable progressDrawable = getProgressDrawable();
        Rect rect = progressDrawable.getBounds();

        int max = getMax();
        int progress = getProgress();

        float x = 0;
        if (max > 0) {
            x = 1f * progress * progressDrawableWidth / max;
        }

        //TODO 待优化：可设置Drawable类型的指示器
        //        canvas.drawBitmap();

        float circleX = x + getPaddingLeft();
        //防止小圆点越界
        if (circleX < indicatorRadius + getPaddingLeft()) {//左越界
            circleX = indicatorRadius + getPaddingLeft();
        } else if (circleX + indicatorRadius > measureWidth - getPaddingRight()) {//右越界
            circleX = (measureWidth - indicatorRadius - getPaddingRight());
        }

        float size = indicatorTextSize * indicatorTextLength; //计算整个progressText的宽度
        float textX = circleX - size / 4;
        //防止text越界
        if (textX < getPaddingLeft()) {//左越界
            textX = indicatorRadius + getPaddingLeft();
        } else if (textX + size / 2  >= measureWidth - getPaddingRight()) { //右越界
            textX = measureWidth - indicatorTextSize * indicatorTextLength / 2 - indicatorRadius * 2 - getPaddingRight();
        }

        canvas.drawCircle(circleX, rect.bottom / 2 + getPaddingTop(), indicatorRadius, indicatorPaint); //画指示器（圆点）
        canvas.drawText(progress + "", textX, rect.bottom + indicatorTextYOffset + +getPaddingTop(), indicatorTextPaint); //画出指示数字

    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);

        updateProgressTextLength(); //刷新前先更新progress text长度

        postInvalidate();
    }

    @Override
    public synchronized void setMax(int max) {
        super.setMax(max);

        postInvalidate();
    }
}
