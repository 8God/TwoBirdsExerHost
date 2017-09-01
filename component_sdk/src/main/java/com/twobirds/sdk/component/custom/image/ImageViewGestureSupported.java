package com.twobirds.sdk.component.custom.image;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.twobirds.sdk.component.custom.guideview.ViewPagerImproved;

/**
 * |  version  |  author  |  version log
 * |   0.0.1   |   TwoBirds    |  初始化
 * |           |          |
 *
 *
 * 支持手势缩放的ImageView（单机缩放、双指缩放）
 *
 * @author TwoBirds
 * @version 0.0.1
 *
 */
public class ImageViewGestureSupported extends ImageView {

    private int imageWidth;
    private int imageHeight;
    private int screenWidth;
    private int screenHeight;

    private float minZoom = 1.0f;
    private float maxZoom = 5.0f;

    private float initTransX = 0f;

    private ViewPagerImproved viewPagerImproved;

    private OnClickListener onClickListener;

    public ImageViewGestureSupported(Context context) {
        super(context);
        init(context);
    }

    public ImageViewGestureSupported(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        //              layoutCenter();

    }

    public void setViewPagerImproved(ViewPagerImproved viewPagerImproved) {
        this.viewPagerImproved = viewPagerImproved;
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        //              layoutCenter();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        //              layoutCenter();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        this.screenHeight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        layoutCenter();

    }

    private void layoutCenter() {
        Drawable drawable = getDrawable();
        if (null != drawable) {
            layoutCenter(getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
        }
    }

    private void init(Context context) {
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(new ImageViewGestureSupportedOnTouchListener(this));
        setLongClickable(true);
    }

    /**
     * 居中图片，缩放图片大小适应屏幕
     *
     * @param imageWidth
     * @param imageHeight
     */
    private void layoutCenter(int imageWidth, int imageHeight) {

        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;

        Matrix matrix = new Matrix();

        matrix.postTranslate((screenWidth - imageWidth) / 2, (screenHeight - imageHeight) / 2);  //宽高度居中

        float scaleX = (screenWidth * 1f) / imageWidth; //适应屏幕宽度的缩放倍数
        float scaleY = (screenHeight * 1f) / imageHeight; //适应屏幕高度的缩放倍数

        minZoom = scaleX / 2; //最小缩放倍数取适应屏幕宽度的缩放倍数的一半
        maxZoom = 1.3f * (scaleX > scaleY ? scaleX : scaleY);//最大缩放倍数取适应屏幕宽度或高度的缩放倍数的1.3倍

        matrix.postScale(scaleX, scaleX, screenWidth / 2, screenHeight / 2);  //根据屏幕宽度缩放图片

        float[] values = new float[9];
        matrix.getValues(values);
        this.initTransX = values[Matrix.MTRANS_X];

        setImageMatrix(matrix);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private class ImageViewGestureSupportedOnTouchListener implements OnTouchListener {

        private Matrix matrix = new Matrix();

        private Matrix savedMatrix = new Matrix(); //用于保存当前图片矩阵

        private static final int NONE = 0; //无模式

        private static final int DRAG = 1; //拖动模式

        private static final int ZOOM = 2; //缩放模式

        private int mode = NONE;  //默认模式为无模式

        // 记录缩放的操作记录
        private PointF start = new PointF();  //开始点，即按下点

        private PointF mid = new PointF();

        private float oldDist = 1f;

        private long lastClickTime = 0;

        private long lastDoubleClickTime = 0;

        private ImageViewGestureSupported imageViewGestureSupported;

        public ImageViewGestureSupportedOnTouchListener(ImageViewGestureSupported imageViewGestureSupported) {
            this.imageViewGestureSupported = imageViewGestureSupported;
        }

        /**
         * 判断touch事件是否发生在图片所在区域
         * @param eventX
         * @param eventY
         * @return
         */
        private boolean isClickImage(float eventX, float eventY) {
            boolean isClickImage = false;

            float[] values = new float[9];
            Matrix imageMatrix = getImageMatrix();
            imageMatrix.getValues(values);

            float scaleX = values[Matrix.MSCALE_X];
            float scaleY = values[Matrix.MSCALE_Y];

            Rect imageBounds = getDrawable() != null ? getDrawable().getBounds() : null;//获取图片原图大小
            if (null != imageBounds) {
                RectF matrixRect = new RectF();
                imageMatrix.mapRect(matrixRect);

                //图片区域右坐标 = 图片区域左坐标 + 原图片宽度 * 横坐标缩放倍数
                matrixRect.right = matrixRect.left + imageBounds.right * scaleX;
                //图片区域底坐标 = 图片区域顶坐标 + 原图片高度 * 纵坐标缩放倍数
                matrixRect.bottom = matrixRect.top + imageBounds.bottom * scaleY;

                //判断触摸事件是否在图片区域
                if (matrixRect.contains(eventX, eventY)) {
                    isClickImage = true;
                }
            }

            return isClickImage;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction() & MotionEvent.ACTION_MASK) { //多点触控action的高两位带序号，必须去除序号再判断

                case MotionEvent.ACTION_DOWN:
                    long currentClickTime = event.getEventTime();
                    if (currentClickTime - lastClickTime < 300 && currentClickTime - lastDoubleClickTime > 500) { //双击缩放条件
                        float[] values = new float[9];

                        matrix.set(savedMatrix);
                        matrix.getValues(values);

                        float transX = 0;
                        float transY = 0;
                        float scaleX = values[Matrix.MSCALE_X];
                        float scaleY = values[Matrix.MSCALE_Y];

                        lastDoubleClickTime = currentClickTime;
                        float zoomScale = minZoom;

                        if (scaleX <= minZoom * 2) { //初始大小是minZoom的两倍，所以等于或小于初始大小，双击都是放大图片
                            zoomScale = maxZoom;
                            transX = (screenWidth / 2 - event.getX()) * (maxZoom / scaleX - 1);
                            transY = (screenHeight / 2 - event.getY()) * (maxZoom / scaleY - 1);
                        }

                        matrix.reset();
                        matrix.postTranslate((screenWidth - imageWidth) / 2, (screenHeight - imageHeight) / 2); //先把矩阵置中，再以屏幕为中心进行缩放
                        matrix.postScale(zoomScale, zoomScale, screenWidth / 2, screenHeight / 2);
                        matrix.postTranslate(transX, transY);

                        savedMatrix.set(matrix);

                        mode = NONE;

                    } else {

                        matrix.set(getImageMatrix());
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        mode = DRAG;
                    }
                    lastClickTime = currentClickTime;

                    break;

                case MotionEvent.ACTION_POINTER_DOWN: // 多点触控
                    oldDist = spacing(event); //计算两指触摸屏幕的距离
                    if (oldDist > 10f) { //大于10像素即进入缩放模式
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;

                    break;

                case MotionEvent.ACTION_UP:
                    //不点击到图片的区域，且没有滑动操作，则促发点击背景回调函数（onClickBackground）
                    if (!isClickImage(event.getX(), event.getY()) && ((event.getX() - start.x < 10) && (event.getY() - start.y < 10))) {
                        if (null != onClickListener) {
                            onClickListener.onClickBackground(imageViewGestureSupported);
                        }

                        return true;
                    }

                    mode = NONE;

                    break;

                case MotionEvent.ACTION_MOVE:

                    if (mode == DRAG) { // 此实现图片的拖动功能...
                        matrix.set(savedMatrix);

                        float[] values = new float[9];
                        matrix.getValues(values);
                        float lastTransX = values[Matrix.MTRANS_X];
                        float lastTransY = values[Matrix.MTRANS_Y];
                        float lastScaleX = values[Matrix.MSCALE_X];
                        float lastScaleY = values[Matrix.MSCALE_Y];

                        Rect imageBounds = getDrawable() != null ? getDrawable().getBounds() : null;
                        if (null != imageBounds) {
                            if (lastScaleX * imageBounds.right > screenWidth || lastScaleY * imageBounds.bottom > screenHeight) {
                                float transX = event.getX() - start.x;
                                if (lastTransX + transX - initTransX > 0) {
                                    transX = initTransX - lastTransX;
                                } else if (imageWidth * lastScaleX + lastTransX + transX < screenWidth - initTransX) {
                                    transX = screenWidth - initTransX - imageWidth * lastScaleX - lastTransX;
                                }

                                float initTransY = 0;
                                float transY = event.getY() - start.y;
                                if (lastScaleY < (screenHeight * 1f) / imageHeight) {
                                    transY = 0;
                                } else {
                                    if (lastTransY + transY - initTransY > 0) {
                                        transY = initTransY - lastTransY;
                                    } else if (imageHeight * lastScaleY + lastTransY + transY < screenHeight - initTransY) {
                                        transY = screenHeight - initTransY - imageHeight * lastScaleY - lastTransY;
                                    }
                                }

                                matrix.postTranslate(transX, transY);
                            } // 图片缩放尺寸大于宽或搞，即可拖动图片
                        }
                    } else if (mode == ZOOM) {// 此实现图片的缩放功能...
                        float newDist = spacing(event);
                        if (newDist > 10) { //手指距离大于初始的10像素

                            matrix.set(savedMatrix);

                            float[] values = new float[9];
                            matrix.getValues(values);
                            float oldScale = values[Matrix.MSCALE_X];
                            float scale = newDist / oldDist * oldScale; //计算缩放倍数
                            if (scale > maxZoom) {
                                scale = maxZoom;
                            } else if (scale < minZoom) {
                                scale = minZoom;
                            }

                            matrix.reset();
                            matrix.postTranslate((screenWidth - imageWidth) / 2, (screenHeight - imageHeight) / 2);
                            matrix.postScale(scale, scale, screenWidth / 2, screenHeight / 2);
                        } // if (newDist > 10)
                    }

                    break;

            } // switch (event.getAction() & MotionEvent.ACTION_MASK)

            setImageMatrix(matrix);

            if (null != viewPagerImproved) {
                float[] values = new float[9];
                matrix.getValues(values);
                if (values[Matrix.MSCALE_X] <= minZoom * 2) {
                    viewPagerImproved.setScrollable(true);
                } else {
                    viewPagerImproved.setScrollable(false);
                }
            }

            return true;
        }

        private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        }

        private void midPoint(PointF point, MotionEvent event) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        }

    }

    public interface OnClickListener {
        void onClickImage(View imageView);

        void onClickBackground(View imageView);
    }

}
