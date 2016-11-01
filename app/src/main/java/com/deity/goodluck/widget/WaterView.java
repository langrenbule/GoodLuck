package com.deity.goodluck.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.deity.goodluck.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 水波纹自定义控件,具体原理可以参考:
 * http://www.jianshu.com/p/cba46422de67
 * Created by Deity on 2016/11/1.
 */

public class WaterView extends View {
    private float initCircleRadius = 400.0f;
    private float maxCircleRadius = 800.0f;
    private float duration = 2*1000;//2秒消失
    private long mCircleCreateSpeed = 500;//创建速度
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();//OvershootInterpolator();//AnticipateInterpolator();//AccelerateDecelerateInterpolator();//AccelerateInterpolator();//DecelerateInterpolator();//LinearInterpolator();
    private Paint mWaterViewPaint;
    private boolean isWaterCircleRunning = true;
    private long mLastCreateTime;
    private List<Circle> mWaterCircleList = new ArrayList<>();
    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public WaterView(Context context) {
        super(context);
    }

    public WaterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mWaterViewPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWaterViewPaint.setColor(getResources().getColor(R.color.redPacketColor));
        mWaterViewPaint.setStyle(Paint.Style.FILL);
    }


    public WaterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WaterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setWaterColor(int color){
        mWaterViewPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Iterator<Circle> circleIterator = mWaterCircleList.iterator();
        while (circleIterator.hasNext()){
            Circle mLuckCircle = circleIterator.next();
            if (System.currentTimeMillis()-mLuckCircle.createTime<duration) {
                float mLuckCircleRadius = mLuckCircle.getCurrentRadius();
                mWaterViewPaint.setAlpha(mLuckCircle.getAlpha());
                canvas.drawCircle(getWidth()/2,getHeight()/2,mLuckCircleRadius,mWaterViewPaint);
            }else {
                circleIterator.remove();
            }
        }
        if (mWaterCircleList.size() > 0) {
            postInvalidateDelayed(100);
        }
    }

    private boolean isOverSpeedTimeSpace(){
        long currentTimeStamp = System.currentTimeMillis();
        if (currentTimeStamp - mLastCreateTime <= mCircleCreateSpeed){
            mLastCreateTime = currentTimeStamp;
            return false;
        }
        return true;
    }

    public void register(){
        if (!isWaterCircleRunning){
            isWaterCircleRunning = true;
        }
        waterCircleRunable.run();
    }

    public void unregister(){
        if (isWaterCircleRunning){
            isWaterCircleRunning = false;
        }
    }

    private void createWaterCircle(){
        boolean isOverSpeedTimeSpace = isOverSpeedTimeSpace();
        if (isOverSpeedTimeSpace){
            Circle newWaterCircle = new Circle();
            mWaterCircleList.add(newWaterCircle);
            invalidate();//立即刷新界面
        }
    }

    private Runnable waterCircleRunable = new Runnable() {
        @Override
        public void run() {
            if (isWaterCircleRunning){
                createWaterCircle();
                postDelayed(waterCircleRunable, mCircleCreateSpeed);
            }
        }
    };

    private class Circle{
        /**单个水波纹的创建时间*/
        private long createTime = System.currentTimeMillis();

        public int getAlpha(){
            float percent = (System.currentTimeMillis()-createTime)*1.0f/duration;
            return (int) ((1.0-mInterpolator.getInterpolation(percent))*255);
        }

        public float getCurrentRadius(){
            float percent = (System.currentTimeMillis()-createTime)*1.0f/duration;
            return initCircleRadius+mInterpolator.getInterpolation(percent)*(maxCircleRadius-initCircleRadius);

        }
    }
}
